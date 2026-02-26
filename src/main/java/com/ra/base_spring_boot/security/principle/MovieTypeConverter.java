package com.ra.base_spring_boot.security.principle;

import com.ra.base_spring_boot.model.constants.MovieType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MovieTypeConverter implements Converter<String, MovieType> {

    @Override
    public MovieType convert(String source) {
        if (source == null) return null;

        String s = source.trim();
        if (s.isEmpty()) return null;

        try {
            // cho phép nhận "_2D"
            if (s.startsWith("_")) {
                return MovieType.valueOf(s);
            }
            // cho phép nhận "2D"
            return MovieType.from(s);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid MovieType: " + source);
        }
    }
}