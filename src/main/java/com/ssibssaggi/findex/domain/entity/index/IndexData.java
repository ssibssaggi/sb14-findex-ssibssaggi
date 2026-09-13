package com.ssibssaggi.findex.domain.entity.index;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class IndexData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate baseDate;
    private SourceType sourceType;
    private BigDecimal marketPrice;
    private BigDecimal closingPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal versus;
    private BigDecimal fluctuationRate;
    private Long tradingQuantity;
    private Long tradingPrice;
    private Long marketTotalAmount;

    @ManyToOne
    @JoinColumn(name = "index_information_id")
    private IndexInformation indexInformation;

    public static IndexData createWithOpenApi(
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
        return new IndexData(
                null,
                baseDate,
                SourceType.OPEN_API,
                marketPrice,
                closingPrice,
                highPrice,
                lowPrice,
                versus,
                fluctuationRate,
                tradingQuantity,
                tradingPrice,
                marketTotalAmount,
                indexInformation
        );
    }
}
