package com.ssibssaggi.findex.controller.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssibssaggi.findex.application.index.dto.DataPoints;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record IndexChartResponse(
        Long indexInfoId,
        String indexClassification,
        String indexName,
        String periodType,
        List<DataPoints> dataPoints,    // 차트 데이터 포인트
        List<DataPoints> ma5DataPoints, // 5일 이동 평균선
        List<DataPoints> ma20DataPoints // 20일 이동 평균선
) {
    public static IndexChartResponse of(
            Long indexInfoId,
            String indexClassification,
            String indexName,
            String periodType,
            List<DataPoints> dataPoints,
            List<DataPoints> ma5DataPoints,
            List<DataPoints> ma20DataPoints
    ) {

        return new IndexChartResponse(
                indexInfoId,
                indexClassification,
                indexName,
                periodType,
                dataPoints,
                ma5DataPoints,
                ma20DataPoints
        );
    }
}
