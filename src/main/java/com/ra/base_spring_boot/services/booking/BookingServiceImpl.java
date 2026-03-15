package com.ra.base_spring_boot.services.booking;

import com.ra.base_spring_boot.dto.req.BookingRequest;
import com.ra.base_spring_boot.dto.resp.BookingResponse;
import com.ra.base_spring_boot.exception.HttpBadRequest;
import com.ra.base_spring_boot.exception.HttpConflict;
import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.exception.HttpUnAuthorized;
import com.ra.base_spring_boot.model.constants.BookingStatus;
import com.ra.base_spring_boot.model.constants.PaymentStatus;
import com.ra.base_spring_boot.model.entity.booking.Booking;
import com.ra.base_spring_boot.model.entity.booking.BookingSeat;
import com.ra.base_spring_boot.model.entity.booking.TicketPrice;
import com.ra.base_spring_boot.model.entity.theater.Seat;
import com.ra.base_spring_boot.model.entity.theater.ShowTime;
import com.ra.base_spring_boot.model.entity.user.User;
import com.ra.base_spring_boot.repository.authrp.IUserRepository;
import com.ra.base_spring_boot.repository.booking.IBookingRepository;
import com.ra.base_spring_boot.repository.booking.IBookingSeatRepository;
import com.ra.base_spring_boot.repository.booking.ISeatRepository;
import com.ra.base_spring_boot.repository.booking.IShowTimeRepository;
import com.ra.base_spring_boot.repository.booking.ITicketPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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

    @Override
    public BookingResponse createBooking(BookingRequest request) {
        User user = getAuthenticatedUser();
        ShowTime showTime = showTimeRepository.findById(request.getShowTimeId())
                .orElseThrow(() -> new HttpNotFound("Showtime not found"));

        validateShowtimeIsBookable(showTime);
        Set<Long> uniqueSeatIds = validateAndNormalizeSeatIds(request);

        Booking booking = Booking.builder()
                .user(user)
                .showTime(showTime)
                .totalSeat(uniqueSeatIds.size())
                .totalAmount(0.0)
                .bookingCode("BK-" + UUID.randomUUID())
                .status(BookingStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        bookingRepository.save(booking);

        double totalAmount = createBookingSeats(booking, showTime, uniqueSeatIds);
        booking.setTotalAmount(totalAmount);

        return mapToResponse(booking);
    }

    @Override
    public BookingResponse completePayment(String bookingCode) {
        Booking booking = bookingRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new HttpNotFound("Booking not found"));

        booking.setStatus(BookingStatus.COMPLETED);
        booking.setPaymentStatus(PaymentStatus.COMPLETED);

        return mapToResponse(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Long id) {
        return mapToResponse(
                bookingRepository.findById(id)
                        .orElseThrow(() -> new HttpNotFound("Booking not found"))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBookingByCode(String bookingCode) {
        return mapToResponse(
                bookingRepository.findByBookingCode(bookingCode)
                        .orElseThrow(() -> new HttpNotFound("Booking not found"))
        );
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new HttpUnAuthorized("Authentication is required");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new HttpUnAuthorized("Authenticated user not found"));
    }

    private void validateShowtimeIsBookable(ShowTime showTime) {
        if (LocalDateTime.now().isAfter(showTime.getStartTime())) {
            throw new HttpBadRequest("Showtime already started");
        }
    }

    private Set<Long> validateAndNormalizeSeatIds(BookingRequest request) {
        if (request.getSeatIds() == null || request.getSeatIds().isEmpty()) {
            throw new HttpBadRequest("At least one seat is required");
        }

        Set<Long> uniqueSeatIds = new HashSet<>(request.getSeatIds());
        if (uniqueSeatIds.size() != request.getSeatIds().size()) {
            throw new HttpBadRequest("Seat list contains duplicate values");
        }
        return uniqueSeatIds;
    }

    private double createBookingSeats(Booking booking, ShowTime showTime, Set<Long> seatIds) {
        LocalDateTime showDateTime = showTime.getStartTime();
        LocalTime showTimeStart = showDateTime.toLocalTime();
        boolean weekend = showDateTime.getDayOfWeek() == DayOfWeek.SATURDAY
                || showDateTime.getDayOfWeek() == DayOfWeek.SUNDAY;

        double totalAmount = 0.0;

        for (Long seatId : seatIds) {
            Seat seat = seatRepository.findByIdForUpdate(seatId)
                    .orElseThrow(() -> new HttpNotFound("Seat not found"));

            validateSeatForShowtime(seat, showTime);
            ensureSeatIsAvailable(seatId, showTime.getId());

            TicketPrice ticketPrice = ticketPriceRepository
                    .findByTypeSeatAndTypeMovieAndDayTypeAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                            seat.getType(),
                            showTime.getMovie().getType(),
                            weekend,
                            showTimeStart,
                            showTimeStart
                    )
                    .orElseThrow(() -> new HttpNotFound("Ticket price not found"));

            BookingSeat bookingSeat = BookingSeat.builder()
                    .booking(booking)
                    .seat(seat)
                    .quantity(1)
                    .price(ticketPrice.getPrice())
                    .createdAt(LocalDateTime.now())
                    .build();

            bookingSeatRepository.save(bookingSeat);
            booking.getBookingSeats().add(bookingSeat);
            totalAmount += ticketPrice.getPrice();
        }

        return totalAmount;
    }

    private void validateSeatForShowtime(Seat seat, ShowTime showTime) {
        if (!seat.getScreen().getId().equals(showTime.getScreen().getId())) {
            throw new HttpBadRequest("Seat does not belong to this showtime screen");
        }

        if (Boolean.TRUE.equals(seat.getIsVariable())) {
            throw new HttpBadRequest("Seat is not available");
        }
    }

    private void ensureSeatIsAvailable(Long seatId, Long showTimeId) {
        boolean seatBooked = bookingSeatRepository
                .existsBySeat_IdAndBooking_ShowTime_IdAndBooking_PaymentStatusIn(
                        seatId,
                        showTimeId,
                        List.of(PaymentStatus.PENDING, PaymentStatus.COMPLETED)
                );

        if (seatBooked) {
            throw new HttpConflict("Seat has already been booked");
        }
    }

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .bookingId(booking.getId())
                .bookingCode(booking.getBookingCode())
                .showTimeId(booking.getShowTime().getId())
                .screenName(booking.getShowTime().getScreen().getName())
                .totalSeat(booking.getTotalSeat())
                .totalAmount(booking.getTotalAmount())
                .status(booking.getStatus().name())
                .paymentStatus(booking.getPaymentStatus() != null ? booking.getPaymentStatus().name() : null)
                .createdAt(booking.getCreatedAt())
                .seatIds(booking.getBookingSeats()
                        .stream()
                        .map(bookingSeat -> bookingSeat.getSeat().getId())
                        .toList())
                .build();
    }
}
