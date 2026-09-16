package com.ssibssaggi.findex.controller.dto;

import com.ssibssaggi.findex.application.index.dto.IndexInfoCreateCommand;

public record IndexInfoCreateRequest(
        String indexName,
        String indexClassification,
        Integer employedItemsCount,
        String basePointInTime,
        Float baseIndex,
        Boolean favorite
) {
    public IndexInfoCreateCommand toCommand() {
        return new IndexInfoCreateCommand(
                indexName, indexClassification, employedItemsCount,
                basePointInTime, baseIndex, favorite
        );
    }
}