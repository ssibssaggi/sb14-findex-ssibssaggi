package com.ssibssaggi.findex.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssibssaggi.findex.controller.dto.AutoSyncConfigDto;
import com.ssibssaggi.findex.controller.dto.AutoSyncConfigUpdateRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AutoSyncController {

    @PatchMapping("/api/auto-sync-configs/{id}")
    public AutoSyncConfigDto updateAutoSyncConfig(
            @PathVariable Long id,
            @RequestBody AutoSyncConfigUpdateRequest request
    ) {
        return null;
    }
}
