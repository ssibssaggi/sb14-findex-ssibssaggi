package com.ssibssaggi.findex.application.indexintegration;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssibssaggi.findex.client.openapi.IndexDataFetchQuery;
import com.ssibssaggi.findex.client.openapi.IndexOpenApiClient;
import com.ssibssaggi.findex.common.dto.CursorPageResult;
import com.ssibssaggi.findex.common.dto.PageMeta;
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
        // 모든 데이터 조회를 위해 추가 조회가 필요한지 확인하기 위해 (요청size + 1)만큼 더 불러온다.
        List<IntegrationHistory> foundEntities = integrationHistoryService.searchIntegrationHistories(
                queryFilterCondition,
                paginationCondition
        );
        Long totalElement = integrationHistoryService.countIntegrationHistories(queryFilterCondition);

        return createCursorPageResult(foundEntities, totalElement, paginationCondition);
    }

    private CursorPageResult<SyncJobDto> createCursorPageResult(
            List<IntegrationHistory> foundEntities,
            Long totalElement,
            CursorPaginationCondition paginationCondition) {

        boolean hasNext = foundEntities.size() > paginationCondition.size();
        List<IntegrationHistory> contents = foundEntities.subList(0,
                Math.min(foundEntities.size(), paginationCondition.size()));
        Long nextIdAfter = null;
        String nextCursor = null;

        if (hasNext) {
            IntegrationHistory lastEntity = contents.get(contents.size() - 1);

            nextIdAfter = lastEntity.getId();
            nextCursor = getLastSortValue(paginationCondition.sortField(), lastEntity);
        }

        PageMeta pageMeta = PageMeta.builder()
                .nextCursor(nextCursor)
                .nextIdAfter(nextIdAfter)
                .size(paginationCondition.size())
                .totalElements(totalElement)
                .hasNext(hasNext)
                .build();

        return CursorPageResult.of(SyncJobDto.from(contents), pageMeta);
    }

    private String getLastSortValue(String sortField, IntegrationHistory integrationHistory) {
        return switch (sortField) {
            case "targetDate" -> integrationHistory.getTargetDate() == null
                    ? "INDEX_INFO"
                    : integrationHistory.getTargetDate().toString();
            case "jobTime" -> integrationHistory.getJobTime().toString();
            default -> null;
        };
    }
}
