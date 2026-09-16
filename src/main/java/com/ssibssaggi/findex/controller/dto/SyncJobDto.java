package com.ssibssaggi.findex.controller.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.annotation.Nullable;

import com.ssibssaggi.findex.domain.entity.integrationhistory.IntegrationHistory;
import com.ssibssaggi.findex.domain.entity.integrationhistory.IntegrationResult;
import com.ssibssaggi.findex.domain.entity.integrationhistory.JobType;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record SyncJobDto(@NonNull Long id,
                         @NonNull JobType jobType,
                         @NonNull Long indexInfoId,
                         @Nullable LocalDate targetDate,    // 지수 데이터 날짜
                         @NonNull String worker,
                         @NonNull LocalDateTime jobTime,
                         @NonNull IntegrationResult result) {

    public static List<SyncJobDto> from(List<IntegrationHistory> integrationHistories) {
        return integrationHistories.stream()
                .map(SyncJobDto::from)
                .toList();
    }

    private static SyncJobDto from(IntegrationHistory integrationHistory) {
        return SyncJobDto.builder()
                .id(integrationHistory.getId())
                .jobType(integrationHistory.getJobtype())
                .indexInfoId(integrationHistory.getIndexInformation().getId())
                .targetDate(integrationHistory.getTargetDate())
                .worker(integrationHistory.getWorker())
                .jobTime(integrationHistory.getJobTime())
                .result(integrationHistory.getResult())
                .build();
    }
}
