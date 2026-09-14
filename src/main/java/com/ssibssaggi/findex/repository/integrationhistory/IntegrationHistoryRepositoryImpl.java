package com.ssibssaggi.findex.repository.integrationhistory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssibssaggi.findex.application.indexintegration.IntegrationHistoryFilterCondition;
import com.ssibssaggi.findex.controller.dto.CursorPaginationCondition;
import com.ssibssaggi.findex.domain.entity.integrationhistory.IntegrationHistory;
import com.ssibssaggi.findex.domain.entity.integrationhistory.IntegrationResult;
import com.ssibssaggi.findex.domain.entity.integrationhistory.JobType;
import com.ssibssaggi.findex.domain.entity.integrationhistory.QIntegrationHistory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class IntegrationHistoryRepositoryImpl implements IntegrationHistoryRepositoryCustom {
    private static final QIntegrationHistory integrationHistory = QIntegrationHistory.integrationHistory;

    private final JPAQueryFactory queryFactory;

    @Override
    public List<IntegrationHistory> searchIntegrationHistories(IntegrationHistoryFilterCondition queryFilterCondition,
            CursorPaginationCondition paginationCondition) {
        JobType jobType = queryFilterCondition.jobType();
        Long indexInfoId = queryFilterCondition.indexInfoId();
        LocalDate baseDateFrom = queryFilterCondition.baseDateFrom();
        LocalDate baseDateTo = queryFilterCondition.baseDateTo();
        IntegrationResult status = queryFilterCondition.status();
        String worker = queryFilterCondition.worker();
        LocalDateTime jobTimeFrom = queryFilterCondition.jobTimeFrom();
        LocalDateTime jobTimeTo = queryFilterCondition.jobTimeTo();

        String sortField = paginationCondition.sortField();
        String sortDirection = paginationCondition.sortDirection();

        return queryFactory.selectFrom(integrationHistory).where(jobTypeEquals(jobType),
                        indexInformationEquals(indexInfoId),
                        resultEquals(status),
                        workerEquals(worker),
                        jobTimeBetween(jobTimeFrom, jobTimeTo),
                        baseDateBetween(baseDateFrom, baseDateTo),
                        cursorCondition(paginationCondition))
                .orderBy(createOrderSpecifiers(sortField, sortDirection))
                .limit(Long.sum(paginationCondition.size(), 1L)).fetch();
    }

    private static BooleanExpression jobTypeEquals(JobType jobType) {
        if (Objects.isNull(jobType)) {
            return null;
        }
        return integrationHistory.jobtype.eq(jobType);
    }

    private static BooleanExpression indexInformationEquals(Long indexInfoId) {
        if (Objects.isNull(indexInfoId)) {
            return null;
        }
        return integrationHistory.indexInformation.id.eq(indexInfoId);
    }

    private static BooleanExpression resultEquals(IntegrationResult status) {
        if (Objects.isNull(status)) {
            return null;
        }
        return integrationHistory.result.eq(status);
    }

    private static BooleanExpression workerEquals(String worker) {
        if (Objects.isNull(worker)) {
            return null;
        }
        return integrationHistory.worker.eq(worker);
    }

    private static BooleanExpression jobTimeBetween(LocalDateTime jobTimeFrom, LocalDateTime jobTimeTo) {
        if (Objects.isNull(jobTimeFrom) || Objects.isNull(jobTimeTo)) {
            return null;
        }
        return integrationHistory.jobTime.between(jobTimeFrom, jobTimeTo);
    }

    private static BooleanExpression baseDateBetween(LocalDate baseDateFrom, LocalDate baseDateTo) {
        if (Objects.isNull(baseDateFrom) || Objects.isNull(baseDateTo)) {
            return null;
        }
        return integrationHistory.targetDate.between(baseDateFrom, baseDateTo);
    }

    @Override
    public Long countIntegrationHistories(IntegrationHistoryFilterCondition queryFilterCondition) {
        JobType jobType = queryFilterCondition.jobType();
        Long indexInfoId = queryFilterCondition.indexInfoId();
        LocalDate baseDateFrom = queryFilterCondition.baseDateFrom();
        LocalDate baseDateTo = queryFilterCondition.baseDateTo();
        IntegrationResult status = queryFilterCondition.status();
        String worker = queryFilterCondition.worker();
        LocalDateTime jobTimeFrom = queryFilterCondition.jobTimeFrom();
        LocalDateTime jobTimeTo = queryFilterCondition.jobTimeTo();

        return queryFactory.select(integrationHistory.count()).from(integrationHistory).where(jobTypeEquals(jobType),
                indexInformationEquals(indexInfoId),
                resultEquals(status),
                workerEquals(worker),
                jobTimeBetween(jobTimeFrom, jobTimeTo),
                baseDateBetween(baseDateFrom, baseDateTo)).fetchOne();
    }

    private BooleanExpression cursorCondition(CursorPaginationCondition condition) {
        Long idAfter = condition.idAfter();
        String cursor = condition.cursor();
        String sortField = condition.sortField();
        String sortDirection = condition.sortDirection();

        if (Objects.isNull(idAfter)) {
            return null;
        }

        boolean desc = "DESC".equalsIgnoreCase(sortDirection);

        if ("targetDate".equals(sortField)
                && (Objects.isNull(cursor) || "INDEX_INFO".equals(cursor))) {
            return integrationHistory.targetDate.isNull()
                    .and(desc ? integrationHistory.id.lt(idAfter) : integrationHistory.id.gt(idAfter));
        }

        return switch (sortField) {
            case "targetDate" -> {
                LocalDate cursorTargetDate = LocalDate.parse(cursor);
                yield desc
                        ? integrationHistory.targetDate.lt(cursorTargetDate)
                        .or(integrationHistory.targetDate.eq(cursorTargetDate)
                                .and(integrationHistory.id.lt(idAfter)))
                        .or(integrationHistory.targetDate.isNull())
                        : integrationHistory.targetDate.isNull()
                                .or(integrationHistory.targetDate.gt(cursorTargetDate))
                                .or(integrationHistory.targetDate.eq(cursorTargetDate)
                                        .and(integrationHistory.id.gt(idAfter)));
            }
            case "jobTime" -> {
                LocalDateTime curSorJobTime = LocalDateTime.parse(condition.cursor());
                yield desc
                        ? integrationHistory.jobTime.lt(curSorJobTime)
                        .or(integrationHistory.jobTime.eq(curSorJobTime).and(integrationHistory.id.lt(idAfter)))
                        : integrationHistory.jobTime.gt(curSorJobTime).or(integrationHistory.jobTime.eq(curSorJobTime)
                                .and(integrationHistory.id.gt(idAfter)));
            }
            default -> null;
        };
    }

    private OrderSpecifier<?>[] createOrderSpecifiers(String sortField, String sortDirection) {
        ComparableExpressionBase<?> target = getSortTarget(sortField);
        Order direction = "DESC".equalsIgnoreCase(sortDirection) ? Order.DESC : Order.ASC;
        return new OrderSpecifier<?>[]{
                new OrderSpecifier<>(direction, target),
                new OrderSpecifier<>(direction, integrationHistory.id)
        };
    }

    private ComparableExpressionBase<?> getSortTarget(String sortField) {
        return switch (sortField) {
            case "targetDate" -> integrationHistory.targetDate;
            case "jobTime" -> integrationHistory.jobTime;
            default -> integrationHistory.jobTime;
        };
    }
}
