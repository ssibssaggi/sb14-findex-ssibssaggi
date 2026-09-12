package com.ssibssaggi.findex.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssibssaggi.findex.controller.dto.IndexDataExportResponse;
import com.ssibssaggi.findex.domain.service.IndexDataService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class IndexDataController {

    private final IndexDataService indexDataService;

    @GetMapping("/api/index-data/export/csv")
    public List<IndexDataExportResponse> export(
            @RequestParam(required = false) Long indexInformationId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        return indexDataService.findAllForExport(indexInformationId, startDate, endDate);
    }
}