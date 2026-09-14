package com.ssibssaggi.findex.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssibssaggi.findex.controller.dto.CursorPaginationCondition;
import com.ssibssaggi.findex.controller.dto.IndexDataFilterCondition;
import com.ssibssaggi.findex.domain.entity.index.IndexData;
import com.ssibssaggi.findex.domain.entity.index.QIndexData;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IndexDataRepositoryImpl implements IndexDataRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<IndexData> searchIndexDatas(
            IndexDataFilterCondition indexDataFilterCondition,
            CursorPaginationCondition cursorPaginationCondition
    ) {
        QIndexData indexData = QIndexData.indexData;

        return jpaQueryFactory
                .select(indexData)
                .from(indexData)
                .where(
                        indexInfoIdEquals(indexDataFilterCondition.indexInfoId()),
                        baseDateBetween(indexDataFilterCondition.startDate(), indexDataFilterCondition.endDate()),
                        cursorCondition(cursorPaginationCondition.idAfter(),
                                cursorPaginationCondition.cursor(),
                                cursorPaginationCondition.sortField(),
                                cursorPaginationCondition.sortDirection())
                )
                .orderBy(createOrderSpecifiers(cursorPaginationCondition.sortField(),
                        cursorPaginationCondition.sortDirection()))
                .limit(cursorPaginationCondition.size() + 1)
                .fetch();
    }

    private BooleanExpression indexInfoIdEquals(Long indexInfoId) {
        return indexInfoId == null ? null : QIndexData.indexData.indexInformation.id.eq(indexInfoId);
    }

    private BooleanExpression baseDateBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return QIndexData.indexData.baseDate.between(startDate, endDate);
        } else {
            return null;
        }
    }

    private BooleanExpression cursorCondition(
            Long lastId,
            String lastSortValue,
            String sortField,
            String sortDirction
    ) {
        if (lastSortValue == null) {
            return null;
        }

        QIndexData indexData = QIndexData.indexData;

        boolean isDesc = "Desc".equalsIgnoreCase(sortDirction);

        return switch (sortField) {
            case "baseDate" -> {
                LocalDate targetDate = LocalDate.parse(lastSortValue);
                yield (isDesc ? indexData.baseDate.lt(targetDate)
                        .or(indexData.baseDate.eq(targetDate).and(indexData.id.lt(lastId)))
                        : indexData.baseDate.gt(targetDate)
                                .or(indexData.baseDate.eq(targetDate).and(indexData.id.gt(lastId))));
            }
            case "marketPrice" -> {
                BigDecimal targetMarketPrice = new BigDecimal(lastSortValue);
                yield (isDesc ? indexData.marketPrice.lt(targetMarketPrice)
                        .or(indexData.marketPrice.eq(targetMarketPrice).and(indexData.id.lt(lastId)))
                        : indexData.marketPrice.gt(targetMarketPrice)
                                .or(indexData.marketPrice.eq(targetMarketPrice)
                                        .and(indexData.id.gt(lastId))));
            }
            case "closingPrice" -> {
                BigDecimal targetClosingPrice = new BigDecimal(lastSortValue);
                yield (isDesc ? indexData.closingPrice.lt(targetClosingPrice)
                        .or(indexData.closingPrice.eq(targetClosingPrice).and(indexData.id.lt(lastId)))
                        : indexData.closingPrice.gt(targetClosingPrice)
                                .or(indexData.closingPrice.eq(targetClosingPrice)
                                        .and(indexData.id.gt(lastId))));
            }
            case "highPrice" -> {
                BigDecimal targetHighPrice = new BigDecimal(lastSortValue);
                yield (isDesc ? indexData.highPrice.lt(targetHighPrice)
                        .or(indexData.highPrice.eq(targetHighPrice).and(indexData.id.lt(lastId)))
                        : indexData.highPrice.gt(targetHighPrice)
                                .or(indexData.highPrice.eq(targetHighPrice)
                                        .and(indexData.id.gt(lastId))));
            }
            case "lowPrice" -> {
                BigDecimal targetLowPrice = new BigDecimal(lastSortValue);
                yield (isDesc ? indexData.lowPrice.lt(targetLowPrice)
                        .or(indexData.lowPrice.eq(targetLowPrice).and(indexData.id.lt(lastId)))
                        : indexData.lowPrice.gt(targetLowPrice)
                                .or(indexData.lowPrice.eq(targetLowPrice)
                                        .and(indexData.id.gt(lastId))));
            }
            case "versus" -> {
                BigDecimal targetVersus = new BigDecimal(lastSortValue);
                yield (isDesc ? indexData.versus.lt(targetVersus)
                        .or(indexData.versus.eq(targetVersus).and(indexData.id.lt(lastId)))
                        : indexData.versus.gt(targetVersus)
                                .or(indexData.versus.eq(targetVersus)
                                        .and(indexData.id.gt(lastId))));
            }
            case "fluctuationRate" -> {
                BigDecimal targetFluctuationRete = new BigDecimal(lastSortValue);
                yield (isDesc ? indexData.fluctuationRate.lt(targetFluctuationRete)
                        .or(indexData.fluctuationRate.eq(targetFluctuationRete).and(indexData.id.lt(lastId)))
                        : indexData.fluctuationRate.gt(targetFluctuationRete)
                                .or(indexData.fluctuationRate.eq(targetFluctuationRete)
                                        .and(indexData.id.gt(lastId))));
            }
            case "tradingQuantity" -> {
                Long targetTradingQuantity = Long.valueOf(lastSortValue);
                yield (isDesc ? indexData.tradingQuantity.lt(targetTradingQuantity)
                        .or(indexData.tradingQuantity.eq(targetTradingQuantity).and(indexData.id.lt(lastId)))
                        : indexData.tradingQuantity.gt(targetTradingQuantity)
                                .or(indexData.tradingQuantity.eq(targetTradingQuantity)
                                        .and(indexData.id.gt(lastId))));
            }
            case "tradingPrice" -> {
                Long targetTradingPrice = Long.valueOf(lastSortValue);
                yield (isDesc ? indexData.tradingPrice.lt(targetTradingPrice)
                        .or(indexData.tradingPrice.eq(targetTradingPrice).and(indexData.id.lt(lastId)))
                        : indexData.tradingPrice.gt(targetTradingPrice)
                                .or(indexData.tradingPrice.eq(targetTradingPrice)
                                        .and(indexData.id.gt(lastId))));
            }
            case "marketTotalAmount" -> {
                Long targetMarketTotalAmount = Long.valueOf(lastSortValue);
                yield (isDesc ? indexData.marketTotalAmount.lt(targetMarketTotalAmount)
                        .or(indexData.marketTotalAmount.eq(targetMarketTotalAmount).and(indexData.id.lt(lastId)))
                        : indexData.marketTotalAmount.gt(targetMarketTotalAmount)
                                .or(indexData.marketTotalAmount.eq(targetMarketTotalAmount)
                                        .and(indexData.id.gt(lastId))));
            }
            default -> isDesc ? indexData.id.lt(lastId) : indexData.id.gt(lastId);
        };
    }

    private OrderSpecifier<?>[] createOrderSpecifiers(String sortField, String sortDirection) {
        ComparableExpressionBase<?> target = getSortTarget(sortField);
        Order direction = "DESC".equalsIgnoreCase(sortDirection) ? Order.DESC : Order.ASC;
        QIndexData indexData = QIndexData.indexData;

        return new OrderSpecifier<?>[]{
                new OrderSpecifier<>(direction, target),
                new OrderSpecifier<>(direction, indexData.id)
        };
    }

    private ComparableExpressionBase<?> getSortTarget(String sortField) {
        QIndexData indexData = QIndexData.indexData;

        return switch (sortField) {
            case "marketPrice" -> indexData.marketPrice;
            case "closingPrice" -> indexData.closingPrice;
            case "highPrice" -> indexData.highPrice;
            case "lowPrice" -> indexData.lowPrice;
            case "versus" -> indexData.versus;
            case "fluctuationRate" -> indexData.fluctuationRate;
            case "tradingQuantity" -> indexData.tradingQuantity;
            case "tradingPrice" -> indexData.closingPrice;
            case "marketTotalAmount" -> indexData.marketTotalAmount;
            default -> indexData.baseDate;
        };
    }

    @Override
    public Long count(IndexDataFilterCondition searchCondition) {
        QIndexData indexData = QIndexData.indexData;

        return jpaQueryFactory
                .select(indexData.count())
                .from(indexData)
                .where(
                        indexInfoIdEquals(searchCondition.indexInfoId()),
                        baseDateBetween(searchCondition.startDate(), searchCondition.endDate())
                )
                .fetchOne();
    }
}
