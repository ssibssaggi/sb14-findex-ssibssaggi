package com.ssibssaggi.findex.client.openapi;

import java.time.LocalDate;

import com.ssibssaggi.findex.domain.entity.index.IndexInformation;

public record IndexDataFetchQuery(
        IndexInformation indexInformation,
        LocalDate baseDateFrom,
        LocalDate baseDateTo
) {
    public static IndexDataFetchQuery of(
            IndexInformation indexInformation,
            LocalDate baseDateFrom,
            LocalDate baseDateTo) {
        return new IndexDataFetchQuery(
                indexInformation,
                baseDateFrom,
                baseDateTo
        );
    }

    public String indexClassification() {
        return indexInformation.getIndexClassification();
    }

    public String indexName() {
        return indexInformation.getIndexName();
    }
}
