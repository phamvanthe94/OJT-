package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.req.ShowTimeRequest;
import com.ra.base_spring_boot.dto.resp.ShowTimeDetailResponse;
import com.ra.base_spring_boot.dto.resp.ShowTimeListResponse;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import com.ra.base_spring_boot.model.entity.theater.Screen;
import com.ra.base_spring_boot.model.entity.theater.ShowTime;
import com.ra.base_spring_boot.repository.IShowTimeAdminRepository;
import com.ra.base_spring_boot.repository.MovieRepository;
import com.ra.base_spring_boot.repository.ScreenRepository;
import com.ra.base_spring_boot.services.IShowTimeAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ShowTimeAdminServiceImpl implements IShowTimeAdminService {

    private final IShowTimeAdminRepository showTimeAdminRepository;
    private final ScreenRepository screenRepository;
    private final MovieRepository movieRepository;

    private void validateTime(java.time.LocalDateTime startTime, java.time.LocalDateTime endTime) {

        if (startTime == null || endTime == null) {
            throw new RuntimeException("Thời gian bắt đầu và kết thúc không được để trống.");
        }

        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
            throw new RuntimeException("Thời gian bắt đầu phải trước thời gian kết thúc.");
        }

        if (startTime.isBefore(LocalDateTime.now()))
            throw new RuntimeException("Thời gian bắt đầu phải sau thời gian hiện tại.");

        if (!endTime.isAfter(startTime))
            throw new RuntimeException("Thời gian kết thúc phải sau thời gian bắt đầu.");

    }

    @Override
    public Page<ShowTimeListResponse> getAllShowTimes(String keyword, int page, int size, String sortBy, String direction) {
        String sortField = (sortBy == null || sortBy.isBlank()) ? "id" : sortBy;
        Sort sort = "desc".equalsIgnoreCase(direction) ?
                Sort.by(sortField).descending() :
                Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), sort);

        Page<ShowTime> result = showTimeAdminRepository.searchShowTime(keyword == null ? "" : keyword.trim(), pageable);

        return result.map(showTime -> ShowTimeListResponse.builder()
                .id(showTime.getId())
                .theaterId(showTime.getScreen().getTheater().getId())
                .theaterName(showTime.getScreen().getTheater().getName())
                .screenId(showTime.getScreen().getId())
                .screenName(showTime.getScreen().getName())
                .movieId(showTime.getMovie().getId())
                .movieTitle(showTime.getMovie().getTitle())
                .startTime(showTime.getStartTime())
                .endTime(showTime.getEndTime())
                .build());
    }

    @Override
    public ShowTimeDetailResponse getDetailShowTime(Long showTimeId) {
        ShowTime showTime = showTimeAdminRepository.findById(showTimeId).
                orElseThrow(() -> new RuntimeException("Không tìm thấy suất chiếu với ID: " + showTimeId));
        return ShowTimeDetailResponse.builder()
                .id(showTime.getId())
                .theaterId(showTime.getScreen().getTheater().getId())
                .theaterName(showTime.getScreen().getTheater().getName())
                .theaterLocation(showTime.getScreen().getTheater().getLocation())
                .screenId(showTime.getScreen().getId())
                .screenName(showTime.getScreen().getName())
                .seatCapacity(showTime.getScreen().getSeatCapacity())
                .movieId(showTime.getMovie().getId())
                .movieTitle(showTime.getMovie().getTitle())
                .startTime(showTime.getStartTime())
                .endTime(showTime.getEndTime())
                .createdAt(showTime.getCreatedAt())
                .updatedAt(showTime.getUpdatedAt())
                .build();
    }

    @Transactional
    @Override
    public ShowTimeDetailResponse createShowTime(ShowTimeRequest showTimeRequest) {

        validateTime(showTimeRequest.getStartTime(), showTimeRequest.getEndTime());

        Screen screen = screenRepository.findById(showTimeRequest.getScreenId())
                .orElseThrow(() -> new RuntimeException("Không tìm tấy phòng chiếu với ID: " + showTimeRequest.getScreenId()));

        Movie movie = movieRepository.findById(showTimeRequest.getMovieId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim với ID: " + showTimeRequest.getMovieId()));

        if (showTimeAdminRepository.existsOverlapping(screen.getId(), null, showTimeRequest.getStartTime(), showTimeRequest.getEndTime())) {
            throw new RuntimeException("Thời gian chiếu bị trùng với một suất chiếu khác trong cùng phòng.");
        }

        ShowTime showTime = ShowTime.builder()
                .screen(screen)
                .movie(movie)
                .startTime(showTimeRequest.getStartTime())
                .endTime(showTimeRequest.getEndTime())
                .createdAt(LocalDateTime.now())
                .build();

        ShowTime savedShowTime = showTimeAdminRepository.save(showTime);


        return ShowTimeDetailResponse.builder()
                .id(savedShowTime.getId())
                .theaterId(savedShowTime.getScreen().getTheater().getId())
                .theaterName(savedShowTime.getScreen().getTheater().getName())
                .theaterLocation(savedShowTime.getScreen().getTheater().getName())
                .screenId(savedShowTime.getScreen().getId())
                .screenName(savedShowTime.getScreen().getName())
                .seatCapacity(savedShowTime.getScreen().getSeatCapacity())
                .movieId(savedShowTime.getMovie().getId())
                .movieTitle(savedShowTime.getMovie().getTitle())
                .startTime(savedShowTime.getStartTime())
                .endTime(savedShowTime.getEndTime())
                .createdAt(savedShowTime.getCreatedAt())
                .updatedAt(savedShowTime.getUpdatedAt())
                .build();
    }

    @Transactional
    @Override
    public ShowTimeDetailResponse updateShowTime(Long showTimeId, ShowTimeRequest showTimeRequest) {

        validateTime(showTimeRequest.getStartTime(), showTimeRequest.getEndTime());

        ShowTime showTime = showTimeAdminRepository.findById(showTimeId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy suất chiếu với ID: " + showTimeId));

        Screen screen = screenRepository.findById(showTimeRequest.getScreenId())
                .orElseThrow(() -> new RuntimeException("Không tìm tấy phòng chiếu với ID: " + showTimeRequest.getScreenId()));

        Movie movie = movieRepository.findById(showTimeRequest.getMovieId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim với ID: "
                        + showTimeRequest.getMovieId()));

        if (showTimeAdminRepository.existsOverlapping(screen.getId(), showTimeId, showTimeRequest.getStartTime(), showTimeRequest.getEndTime())) {
            throw new RuntimeException("Thời gian chiếu bị trùng với một suất chiếu khác trong cùng phòng.");
        }

        showTime.setScreen(screen);
        showTime.setMovie(movie);
        showTime.setStartTime(showTimeRequest.getStartTime());
        showTime.setEndTime(showTimeRequest.getEndTime());
        showTime.setUpdatedAt(LocalDateTime.now());

        ShowTime savedShowTime = showTimeAdminRepository.save(showTime);

        return ShowTimeDetailResponse.builder()
                .id(savedShowTime.getId())
                .theaterId(savedShowTime.getScreen().getTheater().getId())
                .theaterName(savedShowTime.getScreen().getTheater().getName())
                .theaterLocation(savedShowTime.getScreen().getTheater().getName())
                .screenId(savedShowTime.getScreen().getId())
                .screenName(savedShowTime.getScreen().getName())
                .seatCapacity(savedShowTime.getScreen().getSeatCapacity())
                .movieId(savedShowTime.getMovie().getId())
                .movieTitle(savedShowTime.getMovie().getTitle())
                .startTime(savedShowTime.getStartTime())
                .endTime(savedShowTime.getEndTime())
                .createdAt(savedShowTime.getCreatedAt())
                .updatedAt(savedShowTime.getUpdatedAt())
                .build();
    }

    @Transactional
    @Override
    public void deleteShowTime(Long showTimeId) {
        ShowTime showTime = showTimeAdminRepository.findById(showTimeId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy suất chiếu với ID: " + showTimeId));
        showTimeAdminRepository.delete(showTime);

    }
}