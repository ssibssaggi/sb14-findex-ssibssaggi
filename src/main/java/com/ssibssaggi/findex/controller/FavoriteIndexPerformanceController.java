package com.ssibssaggi.findex.controller;

import com.ssibssaggi.findex.application.FavoriteIndexPerformanceApplication;
import com.ssibssaggi.findex.controller.dto.IndexPerformanceFavoriteResponse;
import com.ssibssaggi.findex.domain.entity.index.PeriodType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FavoriteIndexPerformanceController {

    private final FavoriteIndexPerformanceApplication favoriteIndexPerformanceApplication;

    @ResponseStatus(HttpStatus.OK) // 스웨거를 보면 상태코드가 200이므로 HttpStatus.OK
    @GetMapping("/api/index-data/performance/favorite") //스웨거를 보면 Get으로 받아온다
    public List<IndexPerformanceFavoriteResponse> getFavoritePerformance(
            @RequestParam(defaultValue = "DAILY")
            PeriodType periodType
    ) {
        return favoriteIndexPerformanceApplication.getFavoritePerformance(periodType);
    }
}
