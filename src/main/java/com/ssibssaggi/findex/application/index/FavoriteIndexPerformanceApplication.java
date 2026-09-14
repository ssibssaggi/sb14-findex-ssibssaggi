package com.ssibssaggi.findex.application.index;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ssibssaggi.findex.application.index.dto.Performance;
import com.ssibssaggi.findex.application.index.support.PerformanceAssembler;
import com.ssibssaggi.findex.domain.service.IndexDataService;
import com.ssibssaggi.findex.domain.service.index.IndexInformationService;
import com.ssibssaggi.findex.domain.support.IndexDataPair;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteIndexPerformanceApplication {
    private final IndexDataService indexDataService;
    private final IndexInformationService indexInformationService;

    @Transactional
    public List<Performance> getFavoritePerformance(String periodType) {

        List<Long> favoriteIndexInfoIds = indexInformationService.findFavoriteIndexInfoIds();

        List<IndexDataPair> pairs = indexDataService.findFavoritePerformance(favoriteIndexInfoIds, periodType);

        return pairs.stream()
                .map(pair -> PerformanceAssembler.assemble(pair.baseDateData(), pair.beforeDatas()))
                .flatMap(List::stream)
                .toList();
    }
}
