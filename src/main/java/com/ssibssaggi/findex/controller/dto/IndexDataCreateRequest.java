package com.ssibssaggi.findex.controller.dto;

import com.ssibssaggi.findex.application.index.dto.IndexDataCreateCommand;
import java.math.BigDecimal;
import java.time.LocalDate;

public record IndexDataCreateRequest(
    Long indexInfoId,
    LocalDate baseDate,
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

    public IndexDataCreateCommand toCommand() {
        return new IndexDataCreateCommand(
            indexInfoId,
            baseDate,
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