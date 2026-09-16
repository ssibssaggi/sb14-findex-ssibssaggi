package com.ssibssaggi.findex.application.indexintegration.dto;

import java.util.List;

import com.ssibssaggi.findex.client.openapi.IndexDataFetchQuery;
import com.ssibssaggi.findex.client.openapi.dto.indexdata.IndexDataFetchResult;

public record IndexDataFetchBatchResult(
        List<IndexDataFetchResult> successfulResults,
        List<IndexDataFetchQuery> failedQueries
) {
}
