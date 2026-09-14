package com.ssibssaggi.findex.controller;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

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

import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.ssibssaggi.findex.application.index.IndexDataApplication;
import com.ssibssaggi.findex.application.index.dto.Performance;
import com.ssibssaggi.findex.common.dto.CursorPageResult;
import com.ssibssaggi.findex.controller.dto.CursorPaginationCondition;
import com.ssibssaggi.findex.controller.dto.IndexChartResponse;
import com.ssibssaggi.findex.controller.dto.IndexDataCreateRequest;
import com.ssibssaggi.findex.controller.dto.IndexDataExportDto;
import com.ssibssaggi.findex.controller.dto.IndexDataFilterCondition;
import com.ssibssaggi.findex.controller.dto.IndexDataResponse;
import com.ssibssaggi.findex.controller.dto.IndexDataUpdateRequest;
import com.ssibssaggi.findex.controller.dto.IndexPerformanceFavoriteResponse;
import com.ssibssaggi.findex.controller.dto.IndexPerformanceRankResponse;
import com.ssibssaggi.findex.domain.service.IndexDataService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class IndexDataController {

    private final IndexDataService indexDataService;
    private final IndexDataApplication indexDataApplication;

    @GetMapping("/api/index-data/export/csv")
    public void export(
            @RequestParam(required = false) Long indexInformationId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortDirection,
            HttpServletResponse response) throws Exception {

        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=index_data.csv");

        List<IndexDataExportDto> exportData =
                indexDataService.findAllForExport(indexInformationId, startDate, endDate, sortField, sortDirection);

        try (
                OutputStream out = response.getOutputStream();
                Writer writer = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8)) {
            out.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});

            HeaderColumnNameMappingStrategy<IndexDataExportDto> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(IndexDataExportDto.class);
            List<String> headerOrder = List.of(
                    "기준일자", "시가", "종가", "고가", "저가", "전일대비등락", "등락률", "거래량", "거래대금", "시가총액"
            );
            strategy.setColumnOrderOnWrite(Comparator.comparing(headerOrder::indexOf));

            StatefulBeanToCsv<IndexDataExportDto> beanToCsv =
                    new StatefulBeanToCsvBuilder<IndexDataExportDto>(writer)
                            .withApplyQuotesToAll(false)
                            .withMappingStrategy(strategy)
                            .build();
            beanToCsv.write(exportData);
            writer.flush();
        }
    }

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

    @ResponseStatus(HttpStatus.OK) // 스웨거를 보면 상태코드가 200이므로 HttpStatus.OK
    @GetMapping("/api/index-data/performance/favorite") //스웨거를 보면 Get으로 받아온다
    public List<IndexPerformanceFavoriteResponse> getFavoritePerformance(
            @RequestParam(defaultValue = "DAILY")
            String periodType
    ) {
        List<Performance> performances = indexDataApplication.getFavoritePerformance(periodType);
        return performances.stream().map(IndexPerformanceFavoriteResponse::of).toList();
    }
}