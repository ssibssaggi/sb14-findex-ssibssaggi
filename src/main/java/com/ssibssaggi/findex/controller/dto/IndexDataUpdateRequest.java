package com.ssibssaggi.findex.controller.dto;

import java.math.BigDecimal;

import com.ssibssaggi.findex.application.index.dto.IndexDataUpdateCommand;

public record IndexDataUpdateRequest(
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

    public IndexDataUpdateCommand toCommand() {
        return new IndexDataUpdateCommand(
                marketPrice,
                closingPrice,
                highPrice,
                lowPrice,
                versus,
                fluctuationRate,
                tradingQuantity,
                tradingPrice,
                marketTotalAmount
        );
    }
}