package com.ssibssaggi.findex.application.indexintegration;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ssibssaggi.findex.client.openapi.dto.indexdata.IndexDataFetchResult;
import com.ssibssaggi.findex.domain.entity.index.IndexData;
import com.ssibssaggi.findex.domain.entity.index.IndexInformation;

public record InsertIndexDataCommand(
        IndexInformation indexInformation,
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
    public static InsertIndexDataCommand from(IndexDataFetchResult fetchResult) {
        return new InsertIndexDataCommand(
                fetchResult.indexInformation(),
                fetchResult.baseDate(),
                fetchResult.marketPrice(),
                fetchResult.closingPrice(),
                fetchResult.highPrice(),
                fetchResult.lowPrice(),
                fetchResult.versus(),
                fetchResult.fluctuationRate(),
                fetchResult.tradingQuantity(),
                fetchResult.tradingPrice(),
                fetchResult.marketTotalAmount()
        );
    }

    public IndexData toIndexData() {
        return IndexData.createWithOpenApi(
                indexInformation,
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
