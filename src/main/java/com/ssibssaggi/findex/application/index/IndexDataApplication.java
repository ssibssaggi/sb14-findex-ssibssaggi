package com.ssibssaggi.findex.application.index;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssibssaggi.findex.application.index.dto.Performance;
import com.ssibssaggi.findex.application.index.support.PerformanceAssembler;
import com.ssibssaggi.findex.controller.dto.IndexPerformanceRankResponse;
import com.ssibssaggi.findex.domain.entity.index.IndexData;
import com.ssibssaggi.findex.domain.service.IndexDataService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IndexDataApplication {
    private final IndexDataService indexDataService;

    @Transactional
    public List<IndexPerformanceRankResponse> getPerformanceRanking(
            Long indexInfoId, String periodType, Integer limit
    ) {
        LocalDate baseDate = LocalDate.now().minusDays(1); // 전날을 기준

        List<IndexData> baseDateData = indexDataService.findDataByBaseDate(
                baseDate,
                indexInfoId,
                limit
        );

        List<IndexData> periodData = indexDataService.findPeriodDataByBaseDate(
                baseDate,
                indexInfoId,
                periodType,
                limit
        );

        List<Performance> performances = PerformanceAssembler.assemble(baseDateData, periodData);

        return performances.stream().map(performance -> {
            int rank = performances.indexOf(performance);
            return IndexPerformanceRankResponse.of(performance, rank);
        }).toList();
    }
}
