package com.ssibssaggi.findex.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssibssaggi.findex.domain.entity.index.IndexData;
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
    public List<IndexData> findByDate(LocalDate targetDate, Long indexInfoId, Integer limit) {
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

    // 타겟날짜 기준으로 (일간, 주간, 월간)
    @Override
    public List<IndexData> findByDateAndPeriod(LocalDate today,
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
    public List<IndexData> findByDateAndIndexInfoId(
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
}
