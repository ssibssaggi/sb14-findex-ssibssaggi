package com.ssibssaggi.findex.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ssibssaggi.findex.application.index.IndexDataApplication;
import com.ssibssaggi.findex.controller.dto.IndexPerformanceRankResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class IndexDataController {
    private final IndexDataApplication indexDataApplication;

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
}
