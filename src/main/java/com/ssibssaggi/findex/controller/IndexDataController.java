package com.ssibssaggi.findex.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ssibssaggi.findex.application.index.IndexDataApplication;
import com.ssibssaggi.findex.common.dto.CursorPageResult;
import com.ssibssaggi.findex.controller.dto.CursorPaginationCondition;
import com.ssibssaggi.findex.controller.dto.IndexChartResponse;
import com.ssibssaggi.findex.controller.dto.IndexDataCreateRequest;
import com.ssibssaggi.findex.controller.dto.IndexDataFilterCondition;
import com.ssibssaggi.findex.controller.dto.IndexDataResponse;
import com.ssibssaggi.findex.controller.dto.IndexDataUpdateRequest;
import com.ssibssaggi.findex.controller.dto.IndexPerformanceRankResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class IndexDataController {

    private final IndexDataApplication indexDataApplication;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/api/index-data")
    public CursorPageResult<IndexDataResponse> getInfos(
            @RequestParam(required = false) Long indexInfoId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Long idAfter,
            @RequestParam(required = false) String cursor,
            @RequestParam(required = false, defaultValue = "baseDate") String sortField,
            @RequestParam(required = false, defaultValue = "desc") String sortDirection,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {

        IndexDataFilterCondition indexDataFilterCondition = new IndexDataFilterCondition(
                indexInfoId,
                startDate,
                endDate
        );
        CursorPaginationCondition cursorPaginationCondition = new CursorPaginationCondition(
                idAfter,
                cursor,
                sortField,
                sortDirection,
                size
        );
        return indexDataApplication.searchDataInfos(indexDataFilterCondition,
                cursorPaginationCondition);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/api/index-data")
    public IndexDataResponse createIndexData(
            @RequestBody IndexDataCreateRequest createRequest) {
        return indexDataApplication.saveData(createRequest.toCommand());
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/api/index-data/{id}")
    public IndexDataResponse updateIndexData(
            @PathVariable Long id, @RequestBody IndexDataUpdateRequest request) {
        return indexDataApplication.update(id, request.toCommand());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/index-data/{id}")
    public void deleteIndexData(@PathVariable Long id) {
        indexDataApplication.delete(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/api/index-data/performance/rank")
    public List<IndexPerformanceRankResponse> getPerformanceRanking(
            @RequestParam(required = false) Long indexInfoId,
            @RequestParam(required = false, defaultValue = "DAILY") String periodType,
            @RequestParam(required = false, defaultValue = "10") Integer limit
    ) {
        return indexDataApplication.getPerformanceRanking(
                indexInfoId,
                periodType,
                limit
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/api/index-data/{id}/chart")
    public IndexChartResponse getIndexChartData(
            @PathVariable Long id,
            @RequestParam String periodType
    ) {

        return indexDataApplication.getIndexChartData(id, periodType);
    }
}