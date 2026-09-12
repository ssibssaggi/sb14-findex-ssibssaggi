package com.ssibssaggi.findex.application.indexintegration;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssibssaggi.findex.client.openapi.IndexDataFetchQuery;
import com.ssibssaggi.findex.client.openapi.IndexOpenApiClient;
import com.ssibssaggi.findex.common.dto.CursorPageResult;
import com.ssibssaggi.findex.controller.dto.CursorPaginationCondition;
import com.ssibssaggi.findex.controller.dto.SyncJobDto;
import com.ssibssaggi.findex.domain.entity.index.IndexInformation;
import com.ssibssaggi.findex.domain.entity.integrationhistory.IntegrationHistory;
import com.ssibssaggi.findex.domain.service.IndexDataService;
import com.ssibssaggi.findex.domain.service.index.IndexInformationService;
import com.ssibssaggi.findex.domain.service.integrationhistory.IntegrationHistoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IndexIntegrationApplication {
    private final IndexOpenApiClient indexOpenApiClient;
    private final IndexInformationService indexInformationService;
    private final IntegrationHistoryService integrationHistoryService;
    private final IndexDataService indexDataService;

    @Transactional
    public List<SyncJobDto> syncIndexInfoWithOpenApi(String worker) {
        List<UpsertIndexInformationCommand> upsertIndexInformationCommands = indexOpenApiClient.syncIndexInformation()
                .stream()
                .map(UpsertIndexInformationCommand::from)
                .toList();
        List<InsertIntegrationHistoryCommand> insertIntegrationHistoryCommands = indexInformationService
                .upsertInformationByIndexClassificationAndIndexName(upsertIndexInformationCommands).stream()
                .map(indexInformation -> InsertIntegrationHistoryCommand.of(worker, null, indexInformation))
                .toList();
        List<IntegrationHistory> integrationHistories = integrationHistoryService.insertIndexInformationHistory(
                insertIntegrationHistoryCommands);
        return SyncJobDto.from(integrationHistories);
    }

    @Transactional
    public List<SyncJobDto> syncIndexDataWithOpenApi(String worker, SyncIndexDataCommand command) {
        List<IndexInformation> indexInformations = indexInformationService.findAllByIds(command.indexInfosIds());
        List<IndexDataFetchQuery> indexDataFetchQueries = indexInformations.stream()
                .map(eachIndexInfo -> IndexDataFetchQuery.of(
                        eachIndexInfo,
                        command.baseDateFrom(),
                        command.baseDateTo()
                ))
                .toList();

        List<InsertIndexDataCommand> insertIndexDataCommands = indexOpenApiClient.syncIndexData(indexDataFetchQueries)
                .stream()
                .map(InsertIndexDataCommand::from)
                .toList();

        List<InsertIntegrationHistoryCommand> insertIntegrationHistoryCommands = indexDataService
                .insertIndexData(insertIndexDataCommands)
                .stream()
                .map(indexData -> InsertIntegrationHistoryCommand.of(
                        worker,
                        indexData.getBaseDate(),
                        indexData.getIndexInformation()
                ))
                .toList();

        List<IntegrationHistory> integrationHistories = integrationHistoryService.insertIndexDataHistory(
                insertIntegrationHistoryCommands);
        return SyncJobDto.from(integrationHistories);
    }

    @Transactional
    public CursorPageResult<SyncJobDto> querySyncJobs(
            IntegrationHistoryFilterCondition queryFilterCondition,
            CursorPaginationCondition paginationCondition
    ) {
        return null;
    }
}
