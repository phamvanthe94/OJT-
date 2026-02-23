package com.ra.base_spring_boot.services.statisticService.impl;

import com.ra.base_spring_boot.dto.resp.statisticResponse.TicketStatisticExcelView;
import com.ra.base_spring_boot.model.constants.PaymentStatus;
import com.ra.base_spring_boot.model.entity.content.Festival;
import com.ra.base_spring_boot.model.entity.content.News;
import com.ra.base_spring_boot.model.entity.movie.Genre;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import com.ra.base_spring_boot.model.entity.user.User;
import com.ra.base_spring_boot.repository.*;
import com.ra.base_spring_boot.repository.statisticRp.*;
import com.ra.base_spring_boot.services.statisticService.IExportStatisticExcelService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExportStatisticExcelServiceImpl implements IExportStatisticExcelService {

    private final IUserStatisticRepository userRepo;
    private final IMovieStatisticRepository movieRepo;
    private final ITicketStatisticRepository ticketRepo;
    private final IRevenueStatisticRepository revenueRepo;
    private final INewRepository newRepo;
    private final IFestivalRepository festivalRepo;

    @Override
    public ByteArrayInputStream exportStatisticExcel(LocalDateTime from, LocalDateTime to) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            createMovieSheet(workbook,from, to);
            createUserSheet(workbook, from, to);
            createTicketSheet(workbook, from, to);
            createRevenueSheet(workbook, from, to);
            createNewsSheet(workbook, from, to);
            createFestivalSheet(workbook, from, to);

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Export Excel failed", e);
        }
    }

    // ================= MOVIE =================
    private void createMovieSheet(Workbook workbook,
                                  LocalDateTime from,
                                  LocalDateTime to) {

        Sheet sheet = workbook.createSheet("Movie");

        Row h = sheet.createRow(0);
        String[] headers = {
                "No", "ID", "Tên", "Thể loại",
                "Đạo diễn", "Ngày chiếu", "Trạng thái"
        };

        for (int i = 0; i < headers.length; i++)
            h.createCell(i).setCellValue(headers[i]);

        List<Movie> movies =
                movieRepo.findByReleaseDateBetween(from, to);

        int row = 1, no = 1;

        for (Movie m : movies) {
            Row r = sheet.createRow(row++);
            r.createCell(0).setCellValue(no++);
            r.createCell(1).setCellValue(m.getId());
            r.createCell(2).setCellValue(m.getTitle());
            r.createCell(3).setCellValue(
                    m.getGenres().stream()
                            .map(Genre::getGenreName)
                            .collect(Collectors.joining(", "))
            );
            r.createCell(4).setCellValue(m.getAuthor());
            r.createCell(5).setCellValue(String.valueOf(m.getReleaseDate()));
            r.createCell(6).setCellValue(m.getStatus().name());
        }
    }


    // ================= USER =================
    private void createUserSheet(Workbook workbook,
                                 LocalDateTime from,
                                 LocalDateTime to) {

        Sheet sheet = workbook.createSheet("User");

        Row h = sheet.createRow(0);
        String[] headers = {
                "No", "ID", "First Name", "Last Name",
                "Email", "Số điện thoại", "Trạng thái"
        };

        for (int i = 0; i < headers.length; i++)
            h.createCell(i).setCellValue(headers[i]);

        List<User> users =
                userRepo.findByCreatedAtBetween(from, to);

        int row = 1, no = 1;

        for (User u : users) {
            Row r = sheet.createRow(row++);
            r.createCell(0).setCellValue(no++);
            r.createCell(1).setCellValue(u.getId());
            r.createCell(2).setCellValue(u.getFirstName());
            r.createCell(3).setCellValue(u.getLastName());
            r.createCell(4).setCellValue(u.getEmail());
            r.createCell(5).setCellValue(u.getPhone());
            r.createCell(6).setCellValue(u.getStatus().name());
        }
    }


    // ================= TICKET =================
    private void createTicketSheet(
            Workbook workbook,
            LocalDateTime from,
            LocalDateTime to
    ) {
        Sheet sheet = workbook.createSheet("Ticket");

        Row header = sheet.createRow(0);
        String[] headers = {
                "No", "Tên phim", "Loại ghế", "Giá", "Số lượng", "Tổng tiền"
        };
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        // 👉 LẤY DỮ LIỆU ĐÚNG
        List<TicketStatisticExcelView> data =
                ticketRepo.statisticTicketForExcel(
                        PaymentStatus.COMPLETED,
                        from,
                        to
                );

        int rowIdx = 1;
        int no = 1;

        for (TicketStatisticExcelView d : data) {
            Row r = sheet.createRow(rowIdx++);
            r.createCell(0).setCellValue(no++);
            r.createCell(1).setCellValue(d.getMovieTitle());
            r.createCell(2).setCellValue(d.getSeatType().name());
            r.createCell(3).setCellValue(d.getPrice());
            r.createCell(4).setCellValue(d.getQuantity());
            r.createCell(5).setCellValue(d.getTotalAmount());
        }
    }
    // ================= REVENUE =================
    private void createRevenueSheet(
            Workbook workbook,
            LocalDateTime from,
            LocalDateTime to
    ) {
        Sheet sheet = workbook.createSheet("Revenue");

        Row header = sheet.createRow(0);
        String[] headers = {
                "No", "Tên phim", "From", "To", "Tổng doanh thu"
        };

        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        List<Object[]> data = revenueRepo.revenueByMovie(
                PaymentStatus.COMPLETED,
                from,
                to
        );

        int rowIdx = 1;
        int no = 1;

        for (Object[] row : data) {
            Row r = sheet.createRow(rowIdx++);
            r.createCell(0).setCellValue(no++);
            r.createCell(1).setCellValue((String) row[0]);
            r.createCell(2).setCellValue(from.toString());
            r.createCell(3).setCellValue(to.toString());
            r.createCell(4).setCellValue(
                    row[1] == null ? 0 :
                            ((Number) row[1]).doubleValue()
            );
        }
    }

    // ================= NEWS =================
    private void createNewsSheet(Workbook workbook,
                                 LocalDateTime from,
                                 LocalDateTime to) {

        Sheet sheet = workbook.createSheet("News");

        Row h = sheet.createRow(0);
        String[] headers = {"No", "ID", "Tên", "Ngày tạo"};

        for (int i = 0; i < headers.length; i++)
            h.createCell(i).setCellValue(headers[i]);

        List<News> news =
                newRepo.findByCreatedAtBetween(from, to);

        int row = 1, no = 1;

        for (News n : news) {
            Row r = sheet.createRow(row++);
            r.createCell(0).setCellValue(no++);
            r.createCell(1).setCellValue(n.getId());
            r.createCell(2).setCellValue(n.getTitle());
            r.createCell(3).setCellValue(String.valueOf(n.getCreatedAt()));
        }
    }


    // ================= FESTIVAL =================
    private void createFestivalSheet(Workbook workbook,
                                     LocalDateTime from,
                                     LocalDateTime to) {

        Sheet sheet = workbook.createSheet("Festival");

        Row h = sheet.createRow(0);
        String[] headers = {"No", "ID", "Tên", "Ngày bắt đầu"};

        for (int i = 0; i < headers.length; i++)
            h.createCell(i).setCellValue(headers[i]);

        List<Festival> festivals =
                festivalRepo.findByStartTimeBetween(from, to);

        int row = 1, no = 1;

        for (Festival f : festivals) {
            Row r = sheet.createRow(row++);
            r.createCell(0).setCellValue(no++);
            r.createCell(1).setCellValue(f.getId());
            r.createCell(2).setCellValue(f.getTitle());
            r.createCell(3).setCellValue(String.valueOf(f.getStartTime()));
        }
    }

}
