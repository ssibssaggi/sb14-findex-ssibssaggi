package com.ssibssaggi.findex.domain.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ssibssaggi.findex.controller.dto.IndexDataExportDto;
import com.ssibssaggi.findex.repository.IndexDataExportRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IndexDataServiceImplement implements IndexDataService {

    private final IndexDataExportRepository indexDataExportRepository;

    public List<IndexDataExportDto> findAllForExport(Long indexInformationId,
            LocalDate startDate,
            LocalDate endDate) {
        return indexDataExportRepository
                .findByIndexInformationIdAndBaseDateBetween(indexInformationId, startDate, endDate)
                .stream()
                .map(IndexDataExportDto::from)
                .toList();
    }
}