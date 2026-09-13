package com.ssibssaggi.findex.application.index.dto;

import java.math.BigDecimal;

public record IndexDataUpdateCommand(
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

}
