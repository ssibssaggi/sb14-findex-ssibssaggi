package com.ssibssaggi.findex.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssibssaggi.findex.controller.dto.CursorPaginationCondition;
import com.ssibssaggi.findex.controller.dto.IndexDataFilterCondition;
import com.ssibssaggi.findex.domain.entity.index.IndexData;
import com.ssibssaggi.findex.domain.entity.index.IndexInformation;
import com.ssibssaggi.findex.domain.entity.index.PeriodType;
import com.ssibssaggi.findex.domain.entity.index.QIndexData;
import com.ssibssaggi.findex.domain.support.IndexInfoTargetDate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IndexDataCustomRepositoryImpl implements IndexDataCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<LocalDate> findTargetDate(
            LocalDate baseDate, Long indexInfoId
    ) {
        QIndexData indexData = QIndexData.indexData;

        return Optional.ofNullable(
                jpaQueryFactory
                        .select(indexData.baseDate)
                        .from(indexData)
                        .where(
                                indexInfoIdContains(indexInfoId),
                                indexData.baseDate.loe(baseDate) // 휴일일경우 데이터 없으니까 이전꺼를보여줌(<= baseDate)
                        )
                        .orderBy(indexData.baseDate.desc())
                        .limit(1)
                        .fetchFirst()
        );
    }

    @Override
    public List<IndexInfoTargetDate> findTargetDates(
            LocalDate baseDate, List<Long> indexInfoIds
    ) {
        QIndexData indexData = QIndexData.indexData;

        return jpaQueryFactory
                .select(Projections.constructor(
                        IndexInfoTargetDate.class,
                        indexData.indexInformation.id,
                        indexData.baseDate.max()
                ))
                .from(indexData)
                .where(
                        indexData.indexInformation.id.in(indexInfoIds),
                        indexData.baseDate.loe(baseDate)
                )
                .groupBy(indexData.indexInformation.id)
                .fetch();
    }

    @Override
    public List<IndexData> findAllByIndexInfoIdAndDateBetween(
            Long indexInfoId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        QIndexData indexData = QIndexData.indexData;

        return jpaQueryFactory
                .select(indexData)
                .from(indexData)
                .where(
                        indexData.indexInformation.id.eq(indexInfoId),
                        indexData.baseDate.between(startDate, endDate)
                )
                .orderBy(indexData.baseDate.desc())
                .fetch();
    }

    private BooleanExpression indexInfoIdContains(Long value) {
        return value == null ? null : QIndexData.indexData.indexInformation.id.eq(value);
    }

    private BooleanExpression performancePeriodCondition(
            PeriodType periodType,
            LocalDate today
    ) {
        QIndexData indexData = QIndexData.indexData;

        return switch (periodType) {
            case WEEKLY -> {
                LocalDate prevWeek = today.minusWeeks(1);
                yield indexData.baseDate.eq(prevWeek);
            }

            case MONTHLY -> {
                LocalDate prevMonth = today.minusMonths(1);
                yield indexData.baseDate.eq(prevMonth);
            }

            default -> indexData.baseDate.eq(today);
        };
    }

    @Override
    public Optional<LocalDate> findLatestBaseDate(IndexInformation indexInformation) {
        QIndexData indexData = QIndexData.indexData;
        LocalDate latestBaseDate = jpaQueryFactory
                .select(indexData.baseDate.max())
                .from(indexData)
                .where(indexData.indexInformation.eq(indexInformation))
                .fetchOne();
        return Optional.ofNullable(latestBaseDate);
    }

    @Override
    public List<IndexData> findByBaseDate(
            LocalDate targetDate,
            Long indexInfoId,
            Integer limit) {
        QIndexData indexData = QIndexData.indexData;

        JPAQuery<IndexData> query = jpaQueryFactory
                .selectFrom(indexData)
                .where(
                        indexInfoIdContains(indexInfoId),
                        indexData.baseDate.eq(targetDate)
                ).orderBy(indexData.closingPrice.desc());

        if (limit != null) {
            query.limit(limit);
        }

        return query.fetch();
    }

    @Override
    public List<IndexData> findAllByBaseDateAndIndexInfoId(
            LocalDate targetDate,
            Long indexInfoId
    ) {
        QIndexData indexData = QIndexData.indexData;

        return jpaQueryFactory
                .select(indexData)
                .from(indexData)
                .where(
                        indexData.indexInformation.id.eq(indexInfoId),
                        indexData.baseDate.eq(targetDate)
                )
                .fetch();
    }

    @Override
    public List<IndexData> findByBaseDateAndPeriod(LocalDate today,
            Long indexInfoId,
            PeriodType periodType,
            Integer limit
    ) {
        QIndexData indexData = QIndexData.indexData;
        JPAQuery<IndexData> query = jpaQueryFactory
                .selectFrom(indexData)
                .where(
                        indexInfoIdContains(indexInfoId),
                        performancePeriodCondition(periodType, today)
                ).orderBy(indexData.closingPrice.desc());

        if (limit != null) {
            query.limit(limit);
        }

        return query.fetch();
    }

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
            String sortDirection
    ) {
        if (lastSortValue == null) {
            return null;
        }

        QIndexData indexData = QIndexData.indexData;

        boolean isDesc = "Desc".equalsIgnoreCase(sortDirection);

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
            case "tradingPrice" -> indexData.tradingPrice;
            case "marketTotalAmount" -> indexData.marketTotalAmount;
            default -> indexData.baseDate;
        };
    }

    @Override
    public List<IndexData> findByIndexInformationIdAndBaseDateBetween(
            Long indexInformationId, LocalDate startDate, LocalDate endDate, String sortField, String sortDirection) {
        QIndexData indexData = QIndexData.indexData;
        return jpaQueryFactory
                .selectFrom(indexData)
                .where(
                        indexInfoIdEquals(indexInformationId),
                        baseDateBetween(startDate, endDate)
                )
                .orderBy(createOrderSpecifiers(sortField, sortDirection))
                .fetch();
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
