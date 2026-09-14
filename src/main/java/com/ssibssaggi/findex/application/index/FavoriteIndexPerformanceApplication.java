package com.ssibssaggi.findex.application.index;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssibssaggi.findex.controller.dto.IndexPerformanceFavoriteResponse;
import com.ssibssaggi.findex.domain.entity.index.PeriodType;
import com.ssibssaggi.findex.domain.service.IndexDataService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteIndexPerformanceApplication {
    private final IndexDataService indexDataService;

    public List<IndexPerformanceFavoriteResponse> getFavoritePerformance(PeriodType periodType) {
        return indexDataService.findFavoritePerformance(periodType);
    }
}
