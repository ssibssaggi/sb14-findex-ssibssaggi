package com.ssibssaggi.findex.application.indexintegration;

import java.time.LocalDate;

import com.ssibssaggi.findex.client.openapi.dto.indexinfo.IndexInfoFetchResult;

public record UpsertIndexInformationCommand(String indexClassification,
                                            String indexName,
                                            Integer employedItemCount,
                                            LocalDate basePointInTime,
                                            Float baseIndex) {

    public static UpsertIndexInformationCommand from(IndexInfoFetchResult indexInfoApiItem) {
        return new UpsertIndexInformationCommand(
                indexInfoApiItem.indexClassification(),
                indexInfoApiItem.indexName(),
                indexInfoApiItem.employedItemCount(),
                indexInfoApiItem.basePointInTime(),
                indexInfoApiItem.baseIndex()
        );
    }
}
