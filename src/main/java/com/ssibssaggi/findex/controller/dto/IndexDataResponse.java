package com.ssibssaggi.findex.controller.dto;

import com.ssibssaggi.findex.domain.entity.index.IndexData;
import java.math.BigDecimal;
import java.time.LocalDate;

public record IndexDataResponse(
    Long id,
    Long indexInfoId,
    LocalDate baseDate,
    String sourceType,
    BigDecimal marketPrice,
    BigDecimal closingPrice,
    BigDecimal highPrice,
    BigDecimal lowPrice,
    BigDecimal versus,
    BigDecimal fluctuationRate,
    Long tradingQuantity,
    Long tradingPrice,
    Long marketTotalAmount
) {

    public static IndexDataResponse toDto(IndexData indexData) {
        return new IndexDataResponse(
            indexData.getId(),
            indexData.getIndexInformation().getId(),
            indexData.getBaseDate(),
            indexData.getSourceType().toString(),
            indexData.getMarketPrice(),
            indexData.getClosingPrice(),
            indexData.getHighPrice(),
            indexData.getLowPrice(),
            indexData.getVersus(),
            indexData.getFluctuationRate(),
            indexData.getTradingQuantity(),
            indexData.getTradingPrice(),
            indexData.getMarketTotalAmount()
        );
    }
}