package com.ssibssaggi.findex.application.index.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ssibssaggi.findex.domain.entity.index.IndexData;
import com.ssibssaggi.findex.domain.entity.index.IndexInformation;

public record IndexDataCreateCommand(
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

    public IndexData toEntity(IndexInformation indexInformation) {
        return IndexData.createWithUser(
                this.baseDate,
                this.marketPrice,
                this.closingPrice,
                this.highPrice,
                this.lowPrice,
                this.versus,
                this.fluctuationRate,
                this.tradingQuantity,
                this.tradingPrice,
                this.marketTotalAmount,
                indexInformation
        );
    }
}