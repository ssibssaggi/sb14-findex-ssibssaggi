package com.ssibssaggi.findex.domain.service.index;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssibssaggi.findex.common.dto.IndexDataExportDto;
import com.ssibssaggi.findex.repository.IndexDataExportRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DataExportService {

    private final IndexDataExportRepository indexDataExportRepository;

    public List<IndexDataExportDto> getExportData() {
        return indexDataExportRepository.findAll().stream()
                .map(IndexDataExportDto::from)
                .toList();
    }
}
