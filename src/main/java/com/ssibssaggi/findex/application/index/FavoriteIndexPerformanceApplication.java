package com.ssibssaggi.findex.application.index;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ssibssaggi.findex.controller.dto.IndexPerformanceFavoriteResponse;
import com.ssibssaggi.findex.domain.entity.index.IndexInformation;
import com.ssibssaggi.findex.domain.entity.index.PeriodType;
import com.ssibssaggi.findex.domain.service.IndexDataService;
import com.ssibssaggi.findex.domain.service.index.IndexInformationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteIndexPerformanceApplication {
    private final IndexDataService indexDataService;
    private final IndexInformationService indexInformationService;

    @Transactional
    public List<IndexPerformanceFavoriteResponse> getFavoritePerformance(PeriodType periodType) {

        List<Long> favoriteIndexInfoIds = indexInformationService
                .getFavoriteTrue().stream()
                .map(IndexInformation::getId).toList();

        return indexDataService.findFavoritePerformance(favoriteIndexInfoIds, periodType);
    }
}
