package com.ssibssaggi.findex.client.openapi.dto.indexdata;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ssibssaggi.findex.domain.entity.index.IndexInformation;

public record IndexDataFetchResult(
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
    public static IndexDataFetchResult from(
            IndexInformation indexInformation,
            IndexDataOpenApiItem indexData
    ) {
        return new IndexDataFetchResult(
                indexInformation,
                indexData.baseDate(),
                indexData.marketPrice(),
                indexData.closingPrice(),
                indexData.highPrice(),
                indexData.lowPrice(),
                indexData.versus(),
                indexData.fluctuationRate(),
                indexData.tradingQuantity(),
                indexData.tradingPrice(),
                indexData.marketTotalAmount()
        );
    }
}
