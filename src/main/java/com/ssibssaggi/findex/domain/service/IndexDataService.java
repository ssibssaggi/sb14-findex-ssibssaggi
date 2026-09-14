package com.ssibssaggi.findex.domain.service;

import java.time.LocalDate;
import java.util.List;

import com.ssibssaggi.findex.controller.dto.IndexDataExportDto;

public interface IndexDataService {
    List<IndexDataExportDto> findAllForExport(
            Long indexInformationId,
            LocalDate startDate,
            LocalDate endDate
    );
}
