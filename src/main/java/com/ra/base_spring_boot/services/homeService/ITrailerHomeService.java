package com.ra.base_spring_boot.services.homeService;

import com.ra.base_spring_boot.dto.resp.homeresp.MovieTrailerResponse;

public interface ITrailerHomeService {

    
    MovieTrailerResponse getNowShowingMovieTrailer(Long id);


    MovieTrailerResponse getMovieTrailer(Long id);
}
