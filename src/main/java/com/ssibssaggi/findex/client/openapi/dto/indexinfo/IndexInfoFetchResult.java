package com.ssibssaggi.findex.client.openapi.dto.indexinfo;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public record IndexInfoFetchResult(
        @JsonProperty("idxCsf")
        String indexClassification,
        @JsonProperty("idxNm")
        String indexName,
        @JsonProperty("epyItmsCnt")
        Integer employedItemCount,
        @JsonProperty("basPntm")
        @JsonFormat(pattern = "yyyyMMdd")
        LocalDate basePointInTime,
        @JsonProperty("basIdx")
        Float baseIndex
) {
}
