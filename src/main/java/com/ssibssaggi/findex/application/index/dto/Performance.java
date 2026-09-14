package com.ssibssaggi.findex.application.index.dto;

import java.math.BigDecimal;

import com.ssibssaggi.findex.domain.entity.index.IndexInformation;

public record Performance(
        Long indexInfoId,
        String indexClassification,
        String indexName,
        BigDecimal versus,
        BigDecimal fluctuationRate,
        BigDecimal currentPrice,
        BigDecimal beforePrice
) {

    public static Performance of(
            IndexInformation information,
            BigDecimal versus,
            BigDecimal fluctuationRate,
            BigDecimal currentPrice,
            BigDecimal beforePrice) {
        return new Performance(
                information.getId(),
                information.getIndexClassification(),
                information.getIndexName(),
                versus, fluctuationRate, currentPrice, beforePrice
        );
    }

    public static Performance withoutComparison(
            Long indexInfoId,
            String indexClassification,
            String indexName,
            BigDecimal currentPrice) {
        return new Performance(
                indexInfoId,
                indexClassification,
                indexName,
                null,
                null,
                currentPrice,
                null
        );
    }
}
