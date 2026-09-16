package com.ssibssaggi.findex.application.indexintegration;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssibssaggi.findex.application.indexintegration.dto.IndexDataFetchBatchResult;
import com.ssibssaggi.findex.application.indexintegration.dto.InsertIntegrationHistoryCommand;
import com.ssibssaggi.findex.application.indexintegration.dto.IntegrationHistoryFilterCondition;
import com.ssibssaggi.findex.application.indexintegration.dto.SyncIndexDataCommand;
import com.ssibssaggi.findex.application.indexintegration.dto.UpsertIndexDataCommand;
import com.ssibssaggi.findex.application.indexintegration.dto.UpsertIndexInformationCommand;
import com.ssibssaggi.findex.client.openapi.IndexOpenApiClient;
import com.ssibssaggi.findex.client.openapi.dto.indexdata.IndexDataFetchQuery;
import com.ssibssaggi.findex.client.openapi.dto.indexdata.IndexDataFetchResult;
import com.ssibssaggi.findex.common.dto.CursorPageResult;
import com.ssibssaggi.findex.common.dto.PageMeta;
import com.ssibssaggi.findex.common.exception.CustomException;
import com.ssibssaggi.findex.controller.dto.CursorPaginationCondition;
import com.ssibssaggi.findex.controller.dto.SyncJobDto;
import com.ssibssaggi.findex.domain.entity.index.IndexInformation;
import com.ssibssaggi.findex.domain.entity.integrationhistory.IntegrationHistory;
import com.ssibssaggi.findex.domain.service.index.IndexDataService;
import com.ssibssaggi.findex.domain.service.index.IndexInformationService;
import com.ssibssaggi.findex.domain.service.integrationhistory.IntegrationHistoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndexIntegrationApplication {
    private final IndexOpenApiClient indexOpenApiClient;
    private final IndexInformationService indexInformationService;
    private final IntegrationHistoryService integrationHistoryService;
    private final IndexDataService indexDataService;
    private final IndexDataSyncService indexDataSyncService;

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

        return SyncJobDto.from(this.syncIndexData(worker, indexDataFetchQueries));
    }

    @Transactional
    public List<IntegrationHistory> syncAutoSyncEnabledIndexDataWithOpenApi() {
        List<IndexInformation> autoSyncEnabledIndexInfos = indexInformationService.findAllByAutoSyncEnabledIsTrue();

        List<IndexDataFetchQuery> indexDataFetchQueries = autoSyncEnabledIndexInfos.stream()
                .map(indexInformation -> {
                    LocalDate lastFetchedDate = indexDataService.findLatestBaseDate(indexInformation)
                            .orElse(LocalDate.now(ZoneId.systemDefault()).minusDays(8));

                    return IndexDataFetchQuery.fromLastFetchedDate(indexInformation, lastFetchedDate);
                })
                .toList();

        return this.syncIndexData("System", indexDataFetchQueries);
    }

    private List<IntegrationHistory> syncIndexData(String worker, List<IndexDataFetchQuery> queries) {
        IndexDataFetchBatchResult fetchResult = fetchIndexData(queries);
        List<IntegrationHistory> histories = new ArrayList<>();

        // @Transactional
        List<InsertIntegrationHistoryCommand> failedHistoryCommands = fetchResult.failedQueries().stream()
                // 응답 데이터의 있던 없던 기간 사이의 각 날짜에 호출 실패를 기록
                .flatMap(query -> query.baseDateFrom().datesUntil(query.baseDateTo().plusDays(1))
                        .map(targetDate -> InsertIntegrationHistoryCommand.of(
                                worker,
                                targetDate,
                                query.indexInformation())
                        )
                )
                .toList();
        histories.addAll(integrationHistoryService.insertFailedIndexDataHistory(failedHistoryCommands));

        // @Transactional
        List<UpsertIndexDataCommand> upsertIndexDataCommands = fetchResult.successfulResults()
                .stream()
                .map(UpsertIndexDataCommand::from)
                .toList();
        histories.addAll(indexDataSyncService.saveIndexDataWithHistory(worker, upsertIndexDataCommands));
        return histories;
    }

    private IndexDataFetchBatchResult fetchIndexData(List<IndexDataFetchQuery> queries) {
        List<IndexDataFetchResult> results = new ArrayList<>();
        List<IndexDataFetchQuery> failedQueries = new ArrayList<>();

        for (IndexDataFetchQuery query : queries) {
            try {
                results.addAll(indexOpenApiClient.syncIndexData(query));
            } catch (CustomException e) {
                failedQueries.add(query);
                log.warn("[지수 데이터 연동 실패] indexInfoId={}, baseDateFrom={}, baseDateTo={}",
                        query.indexInformation().getId(), query.baseDateFrom(), query.baseDateTo());
            }
        }

        return new IndexDataFetchBatchResult(results, failedQueries);
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
