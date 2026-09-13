package com.ssibssaggi.findex.controller.dto;

import com.ssibssaggi.findex.application.index.dto.Performance;

public record IndexPerformanceRankResponse(
        Performance performance,
        Integer rank
) {
    public static IndexPerformanceRankResponse of(Performance performance, Integer rank) {
        return new IndexPerformanceRankResponse(performance, rank);
    }
}
