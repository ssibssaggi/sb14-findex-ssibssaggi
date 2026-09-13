package com.ssibssaggi.findex.client.openapi.dto.indexdata;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public record IndexDataOpenApiItem(
        @JsonProperty("basDt")
        @JsonFormat(pattern = "yyyyMMdd")
        LocalDate baseDate,
        @JsonProperty("mkp")
        BigDecimal marketPrice,
        @JsonProperty("clpr")
        BigDecimal closingPrice,
        @JsonProperty("hipr")
        BigDecimal highPrice,
        @JsonProperty("lopr")
        BigDecimal lowPrice,
        @JsonProperty("vs")
        BigDecimal versus,
        @JsonProperty("fltRt")
        BigDecimal fluctuationRate,
        @JsonProperty("trqu")
        Long tradingQuantity,
        @JsonProperty("trPrc")
        Long tradingPrice,
        @JsonProperty("lstgMrktTotAmt")
        Long marketTotalAmount,
        @JsonProperty("idxCsf")
        String indexClassification,
        @JsonProperty("idxNm")
        String indexName
) {
    public boolean indexClassificationAndNameEquals(String indexClassification, String indexName) {
        return indexClassification().equals(indexClassification) && indexName().equals(indexName);
    }
}
