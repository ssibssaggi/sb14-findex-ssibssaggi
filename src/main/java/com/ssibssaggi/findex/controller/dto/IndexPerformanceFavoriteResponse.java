package com.ssibssaggi.findex.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record IndexPerformanceFavoriteResponse(
        Long indexInfoId,
        String indexClassification,
        String indexName,
        BigDecimal versus,
        BigDecimal fluctuationRate,
        BigDecimal currentPrice,
        BigDecimal beforePrice
) {
}