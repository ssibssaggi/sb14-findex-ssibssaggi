package com.ssibssaggi.findex.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.ssibssaggi.findex.domain.entity.index.IndexData;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IndexDataExportDto {

    @CsvDate("yyyy-MM-dd")
    @CsvBindByName(column = "기준일자")
    private LocalDate baseDate;

    @CsvBindByName(column = "시가")
    private BigDecimal marketPrice;

    @CsvBindByName(column = "종가")
    private BigDecimal closingPrice;

    @CsvBindByName(column = "고가")
    private BigDecimal highPrice;

    @CsvBindByName(column = "저가")
    private BigDecimal lowPrice;

    @CsvBindByName(column = "전일대비등락")
    private BigDecimal versus;

    @CsvBindByName(column = "등락률")
    private BigDecimal fluctuationRate;

    @CsvBindByName(column = "거래량")
    private Long tradingQuantity;

    @CsvBindByName(column = "거래대금")
    private Long tradingPrice;

    @CsvBindByName(column = "시가총액")
    private Long marketTotalAmount;

    public static IndexDataExportDto from(IndexData indexData) {
        return new IndexDataExportDto(
                indexData.getBaseDate(),
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