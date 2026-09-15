package com.ssibssaggi.findex.application.indexintegration;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssibssaggi.findex.domain.entity.integrationhistory.IntegrationHistory;
import com.ssibssaggi.findex.domain.service.IndexDataService;
import com.ssibssaggi.findex.domain.service.integrationhistory.IntegrationHistoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IndexDataSyncService {
    private final IndexDataService indexDataService;
    private final IntegrationHistoryService integrationHistoryService;

    @Transactional
    public List<IntegrationHistory> saveIndexDataWithHistory(String worker, List<UpsertIndexDataCommand> commands) {
        List<InsertIntegrationHistoryCommand> historyCommands = indexDataService.upsertIndexData(commands)
                .stream()
                .map(indexData -> InsertIntegrationHistoryCommand.of(
                        worker,
                        indexData.getBaseDate(),
                        indexData.getIndexInformation()
                ))
                .toList();

        return integrationHistoryService.insertIndexDataHistory(historyCommands);
    }
}
