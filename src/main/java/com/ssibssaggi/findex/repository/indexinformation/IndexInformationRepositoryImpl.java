package com.ssibssaggi.findex.repository.indexinformation;

import java.util.List;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssibssaggi.findex.controller.dto.CursorPaginationCondition;
import com.ssibssaggi.findex.controller.dto.IndexInfoFilterCondition;
import com.ssibssaggi.findex.domain.entity.index.IndexInformation;
import com.ssibssaggi.findex.domain.entity.index.QAutoSyncConfig;
import com.ssibssaggi.findex.domain.entity.index.QIndexInformation;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IndexInformationRepositoryImpl implements IndexInformationRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<IndexInformation> searchIndexInfos(
            IndexInfoFilterCondition indexInfoFilterCondition,
            CursorPaginationCondition cursorPaginationCondition
    ) {
        QIndexInformation indexInformation = QIndexInformation.indexInformation;

        return jpaQueryFactory
                .select(indexInformation)
                .from(indexInformation)
                .where(
                        indexClassificationContains(indexInfoFilterCondition.indexClassification()),
                        indexNameContains(indexInfoFilterCondition.indexName()),
                        favoriteEquals(indexInfoFilterCondition.favorite()),
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

    private BooleanExpression cursorCondition(
            Long lastId,
            String lastSortValue,
            String sortField,
            String sortDirection
    ) {
        if (lastSortValue == null) {
            return null;
        }

        QIndexInformation indexInformation = QIndexInformation.indexInformation;

        boolean isDesc = "DESC".equalsIgnoreCase(sortDirection);

        return switch (sortField) {
            case "indexClassification" -> isDesc ? indexInformation.indexClassification.lt(lastSortValue)
                    .or(indexInformation.indexClassification.eq(lastSortValue).and(indexInformation.id.lt(lastId)))
                    : indexInformation.indexClassification.gt(lastSortValue)
                            .or(indexInformation.indexClassification.eq(lastSortValue)
                                    .and(indexInformation.id.gt(lastId)));
            case "indexName" -> isDesc ? indexInformation.indexName.lt(lastSortValue)
                    .or(indexInformation.indexName.eq(lastSortValue)
                            .and(indexInformation.id.lt(lastId)))
                    : indexInformation.indexName.gt(lastSortValue)
                            .or(indexInformation.indexName.eq(lastSortValue)
                                    .and(indexInformation.id.gt(lastId)));
            case "employedItemsCount" -> {
                Integer employedItemsCount = Integer.parseInt(lastSortValue);
                yield isDesc ? indexInformation.employedItemsCount.lt(employedItemsCount)
                        .or(indexInformation.employedItemsCount.eq(employedItemsCount)
                                .and(indexInformation.id.lt(lastId)))
                        : indexInformation.employedItemsCount.gt(employedItemsCount)
                                .or(indexInformation.employedItemsCount.eq(employedItemsCount)
                                        .and(indexInformation.id.gt(lastId)));
            }
            default -> isDesc ? indexInformation.id.lt(lastId) : indexInformation.id.gt(lastId);
        };
    }

    @Override
    public Long count(IndexInfoFilterCondition searchCondition) {
        QIndexInformation indexInformation = QIndexInformation.indexInformation;

        return jpaQueryFactory
                .select(indexInformation.count())
                .from(indexInformation)
                .where(
                        indexClassificationContains(searchCondition.indexClassification()),
                        indexNameContains(searchCondition.indexName()),
                        favoriteEquals(searchCondition.favorite())
                )
                .fetchOne();
    }

    // 조건절 (WHERE)
    private BooleanExpression indexClassificationContains(String value) {
        return value == null ? null : QIndexInformation.indexInformation.indexClassification.contains(value);
    }

    private BooleanExpression indexNameContains(String value) {
        return value == null ? null : QIndexInformation.indexInformation.indexName.contains(value);
    }

    private BooleanExpression favoriteEquals(Boolean value) {
        return value == null ? null : QIndexInformation.indexInformation.favorite.eq(value);
    }

    // 정렬 (OrderBy)
    private OrderSpecifier<?>[] createOrderSpecifiers(String sortField, String sortDirection) {
        ComparableExpressionBase<?> target = getSortTarget(sortField);
        Order direction = "DESC".equalsIgnoreCase(sortDirection) ? Order.DESC : Order.ASC;
        QIndexInformation indexInformation = QIndexInformation.indexInformation;

        return new OrderSpecifier<?>[]{
                new OrderSpecifier<>(direction, target),
                new OrderSpecifier<>(direction, indexInformation.id)
        };
    }

    private ComparableExpressionBase<?> getSortTarget(String sortField) {
        QIndexInformation indexInformation = QIndexInformation.indexInformation;

        return switch (sortField) {
            case "indexName" -> indexInformation.indexName;
            case "employedItemsCount" -> indexInformation.employedItemsCount;
            default -> indexInformation.indexClassification;
        };
    }

    @Override
    public List<IndexInformation> findByAutoSyncConfigEnabled(
            CursorPaginationCondition paginationCondition,
            Long indexInfoId,
            Boolean enabled
    ) {
        QIndexInformation indexInformation = QIndexInformation.indexInformation;
        return jpaQueryFactory
                .selectFrom(indexInformation)
                .where(
                        indexInfoIdEquals(indexInfoId, indexInformation),
                        enabledEquals(enabled, indexInformation),
                        autoSyncCursorCondition(paginationCondition.idAfter(),
                                paginationCondition.cursor(),
                                paginationCondition.sortField(),
                                paginationCondition.sortDirection())
                )
                .orderBy(
                        createAutoSyncOrderSpecifiers(
                                paginationCondition.sortField(),
                                paginationCondition.sortDirection()
                        )
                )
                .limit(paginationCondition.size() + 1)
                .fetch();
    }

    private BooleanExpression autoSyncCursorCondition(
            Long lastId,
            String lastSortValue,
            String sortField,
            String sortDirection
    ) {
        if (lastSortValue == null) {
            return null;
        }

        QIndexInformation indexInformation = QIndexInformation.indexInformation;
        boolean isDesc = "DESC".equalsIgnoreCase(sortDirection);
        boolean enabled = Boolean.getBoolean(lastSortValue);

        return switch (sortField) {
            case "enabled" -> isDesc
                    ? indexInformation.autoSyncConfig.enabled.lt(enabled)
                    .or(indexInformation.autoSyncConfig.enabled.eq(enabled).and(indexInformation.id.lt(lastId)))
                    : indexInformation.autoSyncConfig.enabled.gt(enabled)
                            .or(indexInformation.autoSyncConfig.enabled.eq(enabled)
                                    .and(indexInformation.id.gt(lastId)));
            case "indexInfo.indexName" -> isDesc ? indexInformation.indexName.lt(lastSortValue)
                    .or(indexInformation.indexName.eq(lastSortValue)
                            .and(indexInformation.id.lt(lastId)))
                    : indexInformation.indexName.gt(lastSortValue)
                            .or(indexInformation.indexName.eq(lastSortValue)
                                    .and(indexInformation.id.gt(lastId)));
            default -> isDesc ? indexInformation.id.lt(lastId) : indexInformation.id.gt(lastId);
        };
    }

    // 정렬 (OrderBy)
    private OrderSpecifier<?>[] createAutoSyncOrderSpecifiers(String sortField, String sortDirection) {
        ComparableExpressionBase<?> target = getAutoSyncSortTarget(sortField);
        Order direction = "DESC".equalsIgnoreCase(sortDirection) ? Order.DESC : Order.ASC;
        QIndexInformation indexInformation = QIndexInformation.indexInformation;

        return new OrderSpecifier<?>[]{
                new OrderSpecifier<>(direction, target),
                new OrderSpecifier<>(direction, indexInformation.id)
        };
    }

    private ComparableExpressionBase<?> getAutoSyncSortTarget(String sortField) {
        QIndexInformation indexInformation = QIndexInformation.indexInformation;

        return switch (sortField) {
            case "enabled" -> indexInformation.autoSyncConfig.enabled;
            case "indexInfo.indexName" -> indexInformation.indexName;
            default -> indexInformation.indexName;
        };
    }

    @Override
    public Long countByEnabled(Long id, Boolean enabled) {
        QAutoSyncConfig autoSyncConfig = QAutoSyncConfig.autoSyncConfig;
        return jpaQueryFactory
                .select(autoSyncConfig.count())
                .from(autoSyncConfig)
                .where(
                        indexInfoIdEquals(id, autoSyncConfig),
                        enabledEquals(enabled, autoSyncConfig)
                )
                .fetchOne();
    }

    @Override
    public List<Long> findIdsByFavoriteTrue() {
        QIndexInformation indexInformation = QIndexInformation.indexInformation;

        return jpaQueryFactory
                .select(indexInformation.id)
                .from(indexInformation)
                .where(indexInformation.favorite.eq(true))
                .fetch();
    }

    private static BooleanExpression enabledEquals(Boolean enabled, QAutoSyncConfig autoSyncConfig) {
        return enabled == null
                ? null
                : autoSyncConfig.enabled.eq(enabled);
    }

    private static BooleanExpression enabledEquals(Boolean enabled, QIndexInformation indexInformation) {
        return enabled == null
                ? null
                : indexInformation.autoSyncConfig.enabled.eq(enabled);
    }

    private static BooleanExpression indexInfoIdEquals(Long indexInfoId, QAutoSyncConfig autoSyncConfig) {
        return indexInfoId == null
                ? null
                : autoSyncConfig.indexInformation.id.eq(indexInfoId);
    }

    private static BooleanExpression indexInfoIdEquals(Long indexInfoId, QIndexInformation indexInformation) {
        return indexInfoId == null
                ? null
                : indexInformation.id.eq(indexInfoId);
    }
}
