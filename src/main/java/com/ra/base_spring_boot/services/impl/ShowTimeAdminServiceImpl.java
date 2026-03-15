package com.ra.base_spring_boot.services.impl;

import com.ra.base_spring_boot.dto.req.ShowTimeRequest;
import com.ra.base_spring_boot.dto.resp.ShowTimeDetailResponse;
import com.ra.base_spring_boot.dto.resp.ShowTimeListResponse;
import com.ra.base_spring_boot.exception.HttpBadRequest;
import com.ra.base_spring_boot.exception.HttpConflict;
import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import com.ra.base_spring_boot.model.entity.theater.Screen;
import com.ra.base_spring_boot.model.entity.theater.ShowTime;
import com.ra.base_spring_boot.repository.IMovieRepository;
import com.ra.base_spring_boot.repository.IShowTimeAdminRepository;
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
    private final IMovieRepository movieRepository;

    @Override
    public Page<ShowTimeListResponse> getAllShowTimes(String keyword, int page, int size, String sortBy, String direction) {
        String sortField = sortBy == null || sortBy.isBlank() ? "id" : sortBy;
        Sort sort = "desc".equalsIgnoreCase(direction)
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), sort);

        return showTimeAdminRepository.searchShowTime(keyword == null ? "" : keyword.trim(), pageable)
                .map(this::toListResponse);
    }

    @Override
    public ShowTimeDetailResponse getDetailShowTime(Long showTimeId) {
        ShowTime showTime = findShowTime(showTimeId);
        return toDetailResponse(showTime);
    }

    @Transactional
    @Override
    public ShowTimeDetailResponse createShowTime(ShowTimeRequest showTimeRequest) {
        validateTime(showTimeRequest.getStartTime(), showTimeRequest.getEndTime());
        Screen screen = findScreen(showTimeRequest.getScreenId());
        Movie movie = findMovie(showTimeRequest.getMovieId());
        ensureNoOverlap(screen.getId(), null, showTimeRequest);

        ShowTime savedShowTime = showTimeAdminRepository.save(ShowTime.builder()
                .screen(screen)
                .movie(movie)
                .startTime(showTimeRequest.getStartTime())
                .endTime(showTimeRequest.getEndTime())
                .build());

        return toDetailResponse(savedShowTime);
    }

    @Transactional
    @Override
    public ShowTimeDetailResponse updateShowTime(Long showTimeId, ShowTimeRequest showTimeRequest) {
        validateTime(showTimeRequest.getStartTime(), showTimeRequest.getEndTime());

        ShowTime showTime = findShowTime(showTimeId);
        Screen screen = findScreen(showTimeRequest.getScreenId());
        Movie movie = findMovie(showTimeRequest.getMovieId());
        ensureNoOverlap(screen.getId(), showTimeId, showTimeRequest);

        showTime.setScreen(screen);
        showTime.setMovie(movie);
        showTime.setStartTime(showTimeRequest.getStartTime());
        showTime.setEndTime(showTimeRequest.getEndTime());

        return toDetailResponse(showTimeAdminRepository.save(showTime));
    }

    @Transactional
    @Override
    public void deleteShowTime(Long showTimeId) {
        showTimeAdminRepository.delete(findShowTime(showTimeId));
    }

    private void validateTime(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            throw new HttpBadRequest("Start time and end time are required");
        }
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new HttpBadRequest("Start time must be in the future");
        }
        if (!endTime.isAfter(startTime)) {
            throw new HttpBadRequest("End time must be after start time");
        }
    }

    private ShowTime findShowTime(Long showTimeId) {
        return showTimeAdminRepository.findById(showTimeId)
                .orElseThrow(() -> new HttpNotFound("Showtime not found with id = " + showTimeId));
    }

    private Screen findScreen(Long screenId) {
        return screenRepository.findById(screenId)
                .orElseThrow(() -> new HttpNotFound("Screen not found with id = " + screenId));
    }

    private Movie findMovie(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new HttpNotFound("Movie not found with id = " + movieId));
    }

    private void ensureNoOverlap(Long screenId, Long showTimeId, ShowTimeRequest request) {
        if (showTimeAdminRepository.existsOverlapping(
                screenId,
                showTimeId,
                request.getStartTime(),
                request.getEndTime()
        )) {
            throw new HttpConflict("Showtime overlaps with another showtime in the same screen");
        }
    }

    private ShowTimeListResponse toListResponse(ShowTime showTime) {
        return ShowTimeListResponse.builder()
                .id(showTime.getId())
                .theaterId(showTime.getScreen().getTheater().getId())
                .theaterName(showTime.getScreen().getTheater().getName())
                .screenId(showTime.getScreen().getId())
                .screenName(showTime.getScreen().getName())
                .movieId(showTime.getMovie().getId())
                .movieTitle(showTime.getMovie().getTitle())
                .startTime(showTime.getStartTime())
                .endTime(showTime.getEndTime())
                .build();
    }

    private ShowTimeDetailResponse toDetailResponse(ShowTime showTime) {
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
}
