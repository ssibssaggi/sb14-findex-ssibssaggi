package com.ssibssaggi.findex.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ssibssaggi.findex.application.index.IndexInformationApplication;
import com.ssibssaggi.findex.common.dto.CursorPageResult;
import com.ssibssaggi.findex.controller.dto.AutoSyncConfigDto;
import com.ssibssaggi.findex.controller.dto.AutoSyncConfigUpdateRequest;
import com.ssibssaggi.findex.controller.dto.CursorPaginationCondition;
import com.ssibssaggi.findex.controller.swagger.AutoSyncControllerSwagger;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AutoSyncController implements AutoSyncControllerSwagger {
    private final IndexInformationApplication indexInformationApplication;

    @Override
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/api/auto-sync-configs/{id}")
    public AutoSyncConfigDto updateAutoSyncConfig(
            @PathVariable Long id,
            @RequestBody AutoSyncConfigUpdateRequest request
    ) {
        return indexInformationApplication.updateAutoSyncConfig(id, request.enabled());
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/auto-sync-configs")
    public CursorPageResult<AutoSyncConfigDto> searchAutoSyncConfigs(
            @RequestParam(required = false) Long indexInfoId,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) Long idAfter,
            @RequestParam(required = false) String cursor,
            @RequestParam(required = false, defaultValue = "indexInfo.indexName") String sortField,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {

        CursorPaginationCondition paginationCondition = new CursorPaginationCondition(
                idAfter,
                cursor,
                sortField,
                sortDirection,
                size
        );

        return indexInformationApplication.searchSyncConfig(
                indexInfoId,
                enabled,
                paginationCondition
        );
    }
}
