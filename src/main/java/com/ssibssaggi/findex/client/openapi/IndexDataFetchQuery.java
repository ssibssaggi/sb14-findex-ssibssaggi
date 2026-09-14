package com.ssibssaggi.findex.client.openapi;

import java.time.LocalDate;
import java.time.ZoneId;

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

    public static IndexDataFetchQuery fromLastFetchedDate(
            IndexInformation indexInformation,
            LocalDate lastFetchedDate
    ) {
        LocalDate yesterday = LocalDate.now(ZoneId.systemDefault()).minusDays(1L);
        return new IndexDataFetchQuery(
                indexInformation,
                lastFetchedDate.plusDays(1),
                yesterday
        );
    }

    public String indexClassification() {
        return indexInformation.getIndexClassification();
    }

    public String indexName() {
        return indexInformation.getIndexName();
    }
}
