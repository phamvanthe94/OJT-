package com.ra.base_spring_boot.model.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MovieType {
    _2D, _3D;
    @JsonCreator
    public static MovieType from(String value) {
        return MovieType.valueOf("_" + value);
    }

    @JsonValue
    public String toValue() {
        return name().substring(1);
    }
}
