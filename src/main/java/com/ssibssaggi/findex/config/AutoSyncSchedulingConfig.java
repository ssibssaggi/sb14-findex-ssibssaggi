package com.ssibssaggi.findex.config;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ssibssaggi.findex.application.indexintegration.IndexIntegrationApplication;
import com.ssibssaggi.findex.domain.entity.integrationhistory.IntegrationHistory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AutoSyncSchedulingConfig {
    private final IndexIntegrationApplication indexIntegrationApplication;

    //    @Scheduled(cron = "${app.schedule.auto-sync-cron}")
    @Scheduled(fixedRate = 10000)
    public void syncTodayIndexDataWithOpenApi() {
        List<IntegrationHistory> autoSynced = indexIntegrationApplication.syncAutoSyncEnabledIndexDataWithOpenApi();
        log.info("[지수 데이터 자동 연동 실행]: {}", autoSynced);
    }
}
