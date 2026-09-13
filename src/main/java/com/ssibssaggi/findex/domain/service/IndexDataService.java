package com.ssibssaggi.findex.domain.service;

import java.time.LocalDate;
import java.util.List;

import com.ssibssaggi.findex.application.indexintegration.InsertIndexDataCommand;
import com.ssibssaggi.findex.controller.dto.IndexDataExportResponse;
import com.ssibssaggi.findex.domain.entity.index.IndexData;

public interface IndexDataService {
    List<IndexDataExportResponse> findAllForExport(
            Long indexInformationId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<IndexData> insertIndexData(List<InsertIndexDataCommand> insertIndexDataCommands);

    List<IndexData> findPeriodDataByBaseDate(
            LocalDate baseDate,
            Long indexInfoId,
            String periodType,
            Integer limit);

    List<IndexData> findDataByBaseDate(LocalDate baseDate, Long indexInfoId, Integer limit);
}
