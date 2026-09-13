package com.ssibssaggi.findex.controller.dto;

import com.ssibssaggi.findex.domain.entity.index.IndexInformation;

public record AutoSyncConfigDto(
        Long id,
        Long indexInfoId,
        String indexClassification,
        String indexName,
        Boolean enabled
) {
    public static AutoSyncConfigDto of(IndexInformation entity) {
        return new AutoSyncConfigDto(
                entity.getAutoSyncConfig().getId(),
                entity.getId(),
                entity.getIndexClassification(),
                entity.getIndexName(),
                entity.getAutoSyncConfig().isEnabled()
        );
    }
}
