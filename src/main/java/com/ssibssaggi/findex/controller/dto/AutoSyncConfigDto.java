package com.ssibssaggi.findex.controller.dto;

public record AutoSyncConfigDto(
        Long id,
        Long indexInfoId,
        String indexClassification,
        String indexName,
        Boolean enabled
) {
}
