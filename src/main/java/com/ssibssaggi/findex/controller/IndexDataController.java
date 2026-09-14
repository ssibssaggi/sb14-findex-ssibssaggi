package com.ssibssaggi.findex.controller;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.ssibssaggi.findex.controller.dto.IndexDataExportDto;
import com.ssibssaggi.findex.domain.service.IndexDataService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class IndexDataController {

    private final IndexDataService indexDataService;

    @GetMapping("/api/index-data/export/csv")
    public void export(
            @RequestParam(required = false) Long indexInformationId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            HttpServletResponse response) throws Exception {

        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=index_data.csv");
        response.getOutputStream().write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});

        List<IndexDataExportDto> exportData =
                indexDataService.findAllForExport(indexInformationId, startDate, endDate);

        try (Writer writer = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8)) {
            StatefulBeanToCsv<IndexDataExportDto> beanToCsv =
                    new StatefulBeanToCsvBuilder<IndexDataExportDto>(writer)
                            .withApplyQuotesToAll(false)
                            .build();
            beanToCsv.write(exportData);
        }
    }
}