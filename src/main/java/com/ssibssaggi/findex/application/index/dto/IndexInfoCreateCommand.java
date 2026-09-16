package com.ssibssaggi.findex.application.index.dto;

import com.ssibssaggi.findex.domain.entity.index.IndexInformation;

public record IndexInfoCreateCommand(
        String indexName,
        String indexClassification,
        Integer employedItemsCount,
        String basePointInTime,
        Float baseIndex,
        Boolean favorite
) {
    public IndexInformation toEntity() {
        return IndexInformation.createWithUser(
                this.indexName,
                this.indexClassification,
                this.employedItemsCount,
                this.basePointInTime,
                this.baseIndex,
                this.favorite
        );
    }
}
