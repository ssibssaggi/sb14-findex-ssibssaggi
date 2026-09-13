package com.ssibssaggi.findex.domain.service.integrationhistory;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssibssaggi.findex.application.indexintegration.InsertIntegrationHistoryCommand;
import com.ssibssaggi.findex.application.indexintegration.IntegrationHistoryFilterCondition;
import com.ssibssaggi.findex.controller.dto.CursorPaginationCondition;
import com.ssibssaggi.findex.domain.entity.integrationhistory.IntegrationHistory;
import com.ssibssaggi.findex.repository.integrationhistory.IntegrationHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IntegrationHistoryService {
    private final IntegrationHistoryRepository integrationHistoryRepository;

    public List<IntegrationHistory> insertIndexInformationHistory(List<InsertIntegrationHistoryCommand> commands) {
        List<IntegrationHistory> creating = commands.stream()
                .map(command -> IntegrationHistory.createIndexInformationHistory(
                        command.worker(),
                        command.indexInformation()
                ))
                .toList();
        return integrationHistoryRepository.saveAll(creating);
    }

    public List<IntegrationHistory> insertIndexDataHistory(List<InsertIntegrationHistoryCommand> commands) {
        List<IntegrationHistory> creating = commands.stream()
                .map(command -> IntegrationHistory.createIndexDataHistory(
                        command.worker(),
                        command.targetDate(),
                        command.indexInformation()
                ))
                .toList();

        return integrationHistoryRepository.saveAll(creating);
    }

    public List<IntegrationHistory> searchIntegrationHistories(
            IntegrationHistoryFilterCondition queryFilterCondition,
            CursorPaginationCondition paginationCondition) {

        return integrationHistoryRepository.searchIntegrationHistories(
                queryFilterCondition,
                paginationCondition
        );
    }

    public Long countIntegrationHistories(IntegrationHistoryFilterCondition queryFilterCondition) {
        return integrationHistoryRepository.countIntegrationHistories(queryFilterCondition);
    }
}
