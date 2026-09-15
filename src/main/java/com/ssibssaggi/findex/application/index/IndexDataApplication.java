package com.ssibssaggi.findex.application.index;

import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssibssaggi.findex.application.index.dto.DataPoints;
import com.ssibssaggi.findex.application.index.dto.IndexDataCreateCommand;
import com.ssibssaggi.findex.application.index.dto.IndexDataUpdateCommand;
import com.ssibssaggi.findex.application.index.dto.Performance;
import com.ssibssaggi.findex.application.index.support.PerformanceAssembler;
import com.ssibssaggi.findex.common.CsvExporter;
import com.ssibssaggi.findex.common.dto.CursorPageResult;
import com.ssibssaggi.findex.controller.dto.CursorPaginationCondition;
import com.ssibssaggi.findex.controller.dto.IndexChartResponse;
import com.ssibssaggi.findex.controller.dto.IndexDataExportDto;
import com.ssibssaggi.findex.controller.dto.IndexDataFilterCondition;
import com.ssibssaggi.findex.controller.dto.IndexDataResponse;
import com.ssibssaggi.findex.controller.dto.IndexPerformanceRankResponse;
import com.ssibssaggi.findex.domain.entity.index.IndexData;
import com.ssibssaggi.findex.domain.entity.index.IndexInformation;
import com.ssibssaggi.findex.domain.service.IndexDataService;
import com.ssibssaggi.findex.domain.service.index.IndexInformationService;
import com.ssibssaggi.findex.domain.support.IndexDataPair;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IndexDataApplication {

    private final IndexDataService indexDataService;
    private final IndexInformationService indexInformationService;
    private final CsvExporter exporter;

    @Transactional
    public IndexDataResponse saveData(IndexDataCreateCommand createCommand) {
        Long indexInfoId = createCommand.indexInfoId();
        IndexInformation indexInformation = indexInformationService.findById(indexInfoId);
        IndexData savedIndexData = indexDataService.createData(createCommand, indexInformation);

        return IndexDataResponse.toDto(savedIndexData);
    }

    public IndexDataResponse findById(Long id) {
        IndexData indexData = indexDataService.findById(id);

        return IndexDataResponse.toDto(indexData);
    }

    @Transactional
    public IndexDataResponse update(Long id, IndexDataUpdateCommand updateCommand) {
        IndexData indexData = indexDataService.update(id, updateCommand);
        return IndexDataResponse.toDto(indexData);
    }

    @Transactional
    public void delete(Long id) {
        indexDataService.delete(id);
    }

    @Transactional
    public CursorPageResult<IndexDataResponse> searchDataInfos(
            IndexDataFilterCondition indexDataFilterCondition,
            CursorPaginationCondition cursorPaginationCondition
    ) {
        CursorPageResult<IndexData> pageEntities = indexDataService.searchDataInfos(
                indexDataFilterCondition,
                cursorPaginationCondition
        );
        return pageEntities.map(IndexDataResponse::toDto);
    }

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
            int rank = performances.indexOf(performance) + 1;
            return IndexPerformanceRankResponse.of(performance, rank);
        }).toList();
    }

    @Transactional
    public IndexChartResponse getIndexChartData(Long indexInfoId, String periodType) {

        IndexInformation information = indexInformationService.findById(indexInfoId);
        List<IndexData> chartData = indexDataService.getIndexDataChartData(indexInfoId, periodType);

        List<DataPoints> dataPoints = chartData.stream()
                .map(indexData -> DataPoints.of(indexData.getBaseDate().toString(), indexData.getClosingPrice())
                ).toList();
        List<DataPoints> ma5 = indexDataService.calculateMovingAverage(chartData, 5);
        List<DataPoints> ma20 = indexDataService.calculateMovingAverage(chartData, 20);

        return IndexChartResponse.of(
                information.getId(),
                information.getIndexClassification(),
                information.getIndexName(),
                periodType,
                dataPoints,
                ma5,
                ma20

        );
    }

    @Transactional
    public List<Performance> getFavoritePerformance(String periodType) {

        List<Long> favoriteIndexInfoIds = indexInformationService.findFavoriteIndexInfoIds();

        List<IndexDataPair> pairs = indexDataService.findFavoritePerformance(favoriteIndexInfoIds, periodType);

        return pairs.stream()
                .map(pair -> PerformanceAssembler.assemble(pair.baseDateData(), pair.beforeDatas()))
                .flatMap(List::stream)
                .toList();
    }

    @Transactional
    public void findAllForExport(
            Long indexInformationId,
            LocalDate startDate,
            LocalDate endDate,
            String sortField,
            String sortDirection,
            OutputStream out
    ) {
        List<IndexData> entities = indexDataService.findAllForExport(
                indexInformationId,
                startDate,
                endDate,
                sortField,
                sortDirection);
        List<IndexDataExportDto> exportData = entities.stream()
                .map(IndexDataExportDto::from)
                .toList();
        exporter.writeCsv(out, exportData);
    }
}