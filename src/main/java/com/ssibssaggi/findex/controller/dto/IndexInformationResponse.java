package com.ssibssaggi.findex.controller.dto;

import java.time.LocalDate;
import java.util.Optional;

import com.ssibssaggi.findex.domain.entity.index.IndexInformation;

public record IndexInformationResponse(
        Long id,
        String indexClassification,
        String indexName,
        Integer employedItemsCount,
        String basePointInTime,
        Number baseIndex,
        String sourceType,
        Boolean favorite
) {

    public static IndexInformationResponse of(IndexInformation entity) {
        String basePointTime = Optional.ofNullable(entity.getBasePointInTime())
                .map(LocalDate::toString)
                .orElse(null);

        return new IndexInformationResponse(
                entity.getId(),
                entity.getIndexClassification(),
                entity.getIndexName(),
                entity.getEmployedItemsCount(),
                basePointTime,
                entity.getBaseIndex(),
                entity.getSourceType().toString(),
                entity.getFavorite()
        );
    }
}
