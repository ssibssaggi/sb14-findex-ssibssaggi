package com.ssibssaggi.findex.controller.dto;

import com.ssibssaggi.findex.domain.entity.index.IndexInformation;

public record IndexInformationSummaryResponse(
        Long id,
        String indexClassification,
        String indexName
) {

    public static IndexInformationSummaryResponse of(IndexInformation entity) {
        return new IndexInformationSummaryResponse(
                entity.getId(),
                entity.getIndexClassification(),
                entity.getIndexName()
        );
    }
}
