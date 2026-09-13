package com.ssibssaggi.findex.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ssibssaggi.findex.application.indexintegration.IndexIntegrationApplication;
import com.ssibssaggi.findex.application.indexintegration.IntegrationHistoryFilterCondition;
import com.ssibssaggi.findex.application.indexintegration.SyncIndexDataCommand;
import com.ssibssaggi.findex.common.dto.CursorPageResult;
import com.ssibssaggi.findex.controller.dto.CursorPaginationCondition;
import com.ssibssaggi.findex.controller.dto.IndexDataSyncRequest;
import com.ssibssaggi.findex.controller.dto.SyncJobDto;
import com.ssibssaggi.findex.domain.entity.integrationhistory.IntegrationResult;
import com.ssibssaggi.findex.domain.entity.integrationhistory.JobType;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class IndexIntegrationController {
    private final IndexIntegrationApplication indexIntegrationApplication;

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/api/sync-jobs/index-infos")
    public List<SyncJobDto> syncIndexInfo(HttpServletRequest request) {
        return indexIntegrationApplication.syncIndexInfoWithOpenApi(request.getRemoteAddr());
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/api/sync-jobs/index-data")
    public List<SyncJobDto> syncIndexData(
            HttpServletRequest request,
            @RequestBody IndexDataSyncRequest indexDataSyncRequest
    ) {
        SyncIndexDataCommand command = SyncIndexDataCommand.from(indexDataSyncRequest);
        return indexIntegrationApplication.syncIndexDataWithOpenApi(request.getRemoteAddr(), command);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/sync-jobs")
    public CursorPageResult<SyncJobDto> searchSyncJobs(
            @RequestParam(required = false) JobType jobType,
            @RequestParam(required = false) Long indexInfoId,
            @RequestParam(required = false) LocalDate baseDateFrom,
            @RequestParam(required = false) LocalDate baseDateTo,
            @RequestParam(required = false) String worker,
            @RequestParam(required = false) LocalDateTime jobTimeFrom,
            @RequestParam(required = false) LocalDateTime jobTimeTo,
            @RequestParam(required = false) IntegrationResult status,
            @RequestParam(required = false) Long idAfter,
            @RequestParam(required = false) String cursor,
            @RequestParam(required = false, defaultValue = "jobTime") String sortField,
            @RequestParam(required = false, defaultValue = "desc") String sortDirection,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        IntegrationHistoryFilterCondition queryFilterCondition = new IntegrationHistoryFilterCondition(
                jobType,
                indexInfoId,
                baseDateFrom,
                baseDateTo,
                status,
                worker,
                jobTimeFrom,
                jobTimeTo
        );
        CursorPaginationCondition paginationCondition = new CursorPaginationCondition(
                idAfter,
                cursor,
                sortField,
                sortDirection,
                size
        );

        return indexIntegrationApplication.querySyncJobs(queryFilterCondition, paginationCondition);
    }
}
