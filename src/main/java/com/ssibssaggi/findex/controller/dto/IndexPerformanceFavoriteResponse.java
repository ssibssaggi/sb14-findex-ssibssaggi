package com.ssibssaggi.findex.controller.dto;

import java.math.BigDecimal;

import com.ssibssaggi.findex.application.index.dto.Performance;

public record IndexPerformanceFavoriteResponse(
        Long indexInfoId,
        String indexClassification,
        String indexName,
        BigDecimal versus,
        BigDecimal fluctuationRate,
        BigDecimal currentPrice,
        BigDecimal beforePrice
) {

    public static IndexPerformanceFavoriteResponse of(Performance performance) {
        return new IndexPerformanceFavoriteResponse(
                performance.indexInfoId(),
                performance.indexClassification(),
                performance.indexName(),
                performance.versus(),
                performance.fluctuationRate(),
                performance.currentPrice(),
                performance.beforePrice()
        );
    }
}