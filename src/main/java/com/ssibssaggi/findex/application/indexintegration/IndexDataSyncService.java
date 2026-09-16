package com.ssibssaggi.findex.application.indexintegration;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ssibssaggi.findex.application.indexintegration.dto.InsertIntegrationHistoryCommand;
import com.ssibssaggi.findex.application.indexintegration.dto.UpsertIndexDataCommand;
import com.ssibssaggi.findex.domain.entity.integrationhistory.IntegrationHistory;
import com.ssibssaggi.findex.domain.service.index.IndexDataService;
import com.ssibssaggi.findex.domain.service.integrationhistory.IntegrationHistoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IndexDataSyncService {
    private final IndexDataService indexDataService;
    private final IntegrationHistoryService integrationHistoryService;

    // Application Layer에 트랜잭션이 적용되지 않으므로, Service layer에서 연동 성공한 indexDate의 저장, 연동 기록 적재 원자성을 보장
    @Transactional(propagation = Propagation.REQUIRES_NEW)
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
