package com.ssibssaggi.findex.application.index.dto;

import java.math.BigDecimal;

public record DataPoints(
        String date,
        BigDecimal value
) {
    public static DataPoints of(String date, BigDecimal value) {
        return new DataPoints(date, value);
    }
}
