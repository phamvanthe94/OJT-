package com.ra.base_spring_boot.services.booking;

import com.ra.base_spring_boot.dto.req.BookingRequest;
import com.ra.base_spring_boot.dto.resp.BookingResponse;
import com.ra.base_spring_boot.model.constants.BookingStatus;
import com.ra.base_spring_boot.model.constants.PaymentStatus;
import com.ra.base_spring_boot.model.entity.booking.Booking;
import com.ra.base_spring_boot.model.entity.booking.BookingSeat;
import com.ra.base_spring_boot.model.entity.booking.TicketPrice;
import com.ra.base_spring_boot.model.entity.theater.Seat;
import com.ra.base_spring_boot.model.entity.theater.ShowTime;
import com.ra.base_spring_boot.model.entity.user.User;
import com.ra.base_spring_boot.repository.authrp.IUserRepository;
import com.ra.base_spring_boot.repository.booking.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements IBookingService {

    private final IBookingRepository bookingRepository;
    private final IBookingSeatRepository bookingSeatRepository;
    private final ITicketPriceRepository ticketPriceRepository;
    private final ISeatRepository seatRepository;
    private final IShowTimeRepository showTimeRepository;
    private final IUserRepository userRepository;

    // =========================
    // MAP TO RESPONSE
    // =========================
    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .bookingId(booking.getId())
                .bookingCode(booking.getBookingCode())
                .showTimeId(booking.getShowTime().getId())
                .screenName(booking.getShowTime().getScreen().getName())
                .totalSeat(booking.getTotalSeat())
                .totalAmount(booking.getTotalAmount())
                .status(booking.getStatus().name())
                .paymentStatus(
                        booking.getPaymentStatus() != null
                                ? booking.getPaymentStatus().name()
                                : null
                )
                .qrCode(booking.getQrCode())
                .createdAt(booking.getCreatedAt())
                .seatIds(
                        booking.getBookingSeats()
                                .stream()
                                .map(bs -> bs.getSeat().getId())
                                .toList()
                )
                .build();
    }

    // =========================
    // CREATE BOOKING
    // =========================
    @Override
    public BookingResponse createBooking(BookingRequest request) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ShowTime showTime = showTimeRepository.findById(request.getShowTimeId())
                .orElseThrow(() -> new RuntimeException("ShowTime not found"));

        LocalDateTime now = LocalDateTime.now();

        // 1️⃣ Suất chiếu đã bắt đầu chưa
        if (now.isAfter(showTime.getStartTime())) {
            throw new RuntimeException("ShowTime already started");
        }

        // 2️⃣ Kiểm tra phòng thuộc đúng rạp
        if (!showTime.getScreen().getTheater().getId()
                .equals(showTime.getTheater().getId())) {
            throw new RuntimeException("Dữ liệu phòng chiếu và rạp không hợp lệ");
        }

        // 3️⃣ Kiểm tra seatIds trùng trong request
        Set<Long> uniqueSeatIds = new HashSet<>(request.getSeatIds());
        if (uniqueSeatIds.size() != request.getSeatIds().size()) {
            throw new RuntimeException("Danh sách ghế bị trùng");
        } //co the co hoac khong

        String bookingCode = "BK-" + UUID.randomUUID();

        Booking booking = Booking.builder()
                .bookingCode(bookingCode)
                .user(user)
                .showTime(showTime)
                .totalSeat(uniqueSeatIds.size())
                .totalAmount(0.0)
                .status(BookingStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        bookingRepository.save(booking);

        LocalDateTime showDateTime = showTime.getStartTime();
        LocalTime showTimeStart = showDateTime.toLocalTime();

        boolean dayType =
                showDateTime.getDayOfWeek() == DayOfWeek.SATURDAY ||
                        showDateTime.getDayOfWeek() == DayOfWeek.SUNDAY;

        double totalAmount = 0.0;

        for (Long seatId : uniqueSeatIds) {

            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy chỗ ngồi"));

            // 4️⃣ Ghế phải thuộc đúng phòng chiếu
            if (!seat.getScreen().getId()
                    .equals(showTime.getScreen().getId())) {
                throw new RuntimeException("Ghế không thuộc phòng chiếu của suất này");
            }

            // 5️⃣ Ghế phải đang hoạt động
            if (seat.getIsVariable()) {
                throw new RuntimeException("Ghế không khả dụng");

            }

            // 6️⃣ Kiểm tra ghế đã bị đặt chưa
            boolean seatBooked = bookingSeatRepository
                    .existsBySeat_IdAndBooking_ShowTime_IdAndBooking_PaymentStatusIn(
                            seatId,
                            showTime.getId(),
                            List.of(PaymentStatus.PENDING, PaymentStatus.COMPLETED)
                    );

            if (seatBooked) {
                throw new RuntimeException("Ghế đã có người đặt trước rồi");
            }



            // 7️⃣ Lấy giá
            TicketPrice ticketPrice = ticketPriceRepository
                    .findByTypeSeatAndTypeMovieAndDayTypeAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                            seat.getType(),
                            showTime.getMovie().getType(),
                            dayType,
                            showTimeStart,
                            showTimeStart
                    )
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy giá vé"));

            Double price = ticketPrice.getPrice();

            BookingSeat bookingSeat = BookingSeat.builder()
                    .booking(booking)
                    .seat(seat)
                    .price(price)
                    .createdAt(LocalDateTime.now())
                    .build();

            bookingSeatRepository.save(bookingSeat);
            booking.getBookingSeats().add(bookingSeat);

            totalAmount += price;
        }

        booking.setTotalAmount(totalAmount);

        return mapToResponse(booking);
    }

    // =========================
    // COMPLETE PAYMENT
    // =========================
    @Override
    public BookingResponse completePayment(String bookingCode) {

        Booking booking = bookingRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đặt phòng"));

        booking.setStatus(BookingStatus.COMPLETED);
        booking.setPaymentStatus(PaymentStatus.COMPLETED);
        booking.setQrCode("QR|" + booking.getBookingCode());

        return mapToResponse(booking);
    }

    // =========================
    // GET BOOKING
    // =========================
    @Override
    public BookingResponse getBookingById(Long id) {
        return mapToResponse(
                bookingRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy đặt phòng"))
        );
    }

    @Override
    public BookingResponse getBookingByCode(String bookingCode) {
        return mapToResponse(
                bookingRepository.findByBookingCode(bookingCode)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy đặt phòng"))
        );
    }
}
