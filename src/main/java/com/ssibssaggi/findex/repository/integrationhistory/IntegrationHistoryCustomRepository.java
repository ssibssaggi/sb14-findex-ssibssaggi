package com.ssibssaggi.findex.repository.integrationhistory;

import java.util.List;

import com.ssibssaggi.findex.application.indexintegration.dto.IntegrationHistoryFilterCondition;
import com.ssibssaggi.findex.controller.dto.CursorPaginationCondition;
import com.ssibssaggi.findex.domain.entity.integrationhistory.IntegrationHistory;

public interface IntegrationHistoryCustomRepository {
    List<IntegrationHistory> searchIntegrationHistories(
            IntegrationHistoryFilterCondition queryFilterCondition,
            CursorPaginationCondition paginationCondition
    );

    Long countIntegrationHistories(IntegrationHistoryFilterCondition queryFilterCondition);
}
