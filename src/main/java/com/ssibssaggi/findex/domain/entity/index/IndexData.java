package com.ssibssaggi.findex.domain.entity.index;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"index_information_id", "base_date"})
    })
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

    public static IndexData createWithUser(
        LocalDate baseDate,
        BigDecimal marketPrice,
        BigDecimal closingPrice,
        BigDecimal highPrice,
        BigDecimal lowPrice,
        BigDecimal versus,
        BigDecimal fluctuationRate,
        Long tradingQuantity,
        Long tradingPrice,
        Long marketTotalAmount,
        IndexInformation indexInformation) {
        IndexData entity = new IndexData();
        entity.baseDate = baseDate;
        entity.sourceType = SourceType.USER;
        entity.marketPrice = marketPrice;
        entity.closingPrice = closingPrice;
        entity.highPrice = highPrice;
        entity.lowPrice = lowPrice;
        entity.versus = versus;
        entity.fluctuationRate = fluctuationRate;
        entity.tradingQuantity = tradingQuantity;
        entity.tradingPrice = tradingPrice;
        entity.marketTotalAmount = marketTotalAmount;
        entity.indexInformation = indexInformation;
        return entity;
    }

    public void update(
        BigDecimal marketPrice,
        BigDecimal closingPrice,
        BigDecimal highPrice,
        BigDecimal lowPrice,
        BigDecimal versus,
        BigDecimal fluctuationRate,
        Long tradingQuantity,
        Long tradingPrice,
        Long marketTotalAmount) {
        this.marketPrice = marketPrice;
        this.closingPrice = closingPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.versus = versus;
        this.fluctuationRate = fluctuationRate;
        this.tradingQuantity = tradingQuantity;
        this.tradingPrice = tradingPrice;
        this.marketTotalAmount = marketTotalAmount;
        this.sourceType = SourceType.USER;
    }
}
