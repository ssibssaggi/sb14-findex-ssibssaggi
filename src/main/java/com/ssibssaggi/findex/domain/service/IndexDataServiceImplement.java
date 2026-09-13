package com.ssibssaggi.findex.domain.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ssibssaggi.findex.application.indexintegration.InsertIndexDataCommand;
import com.ssibssaggi.findex.controller.dto.IndexDataExportResponse;
import com.ssibssaggi.findex.domain.entity.index.IndexData;
import com.ssibssaggi.findex.repository.IndexDataRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IndexDataServiceImplement implements IndexDataService {

    private final IndexDataRepository indexDataRepository;

    @Override
    public List<IndexDataExportResponse> findAllForExport(Long indexInformationId,
            LocalDate startDate,
            LocalDate endDate) {
        return indexDataRepository
                .findByIndexInformationIdAndBaseDateBetween(indexInformationId, startDate, endDate)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private IndexDataExportResponse toResponse(IndexData data) {
        return new IndexDataExportResponse(
                data.getBaseDate(),
                data.getMarketPrice(),
                data.getClosingPrice(),
                data.getHighPrice(),
                data.getLowPrice(),
                data.getVersus(),
                data.getFluctuationRate(),
                data.getTradingQuantity(),
                data.getTradingPrice(),
                data.getMarketTotalAmount()
        );
    }

    // 정의되어있는 repository 이름이 이상한듯?
    @Override
    public List<IndexData> insertIndexData(List<InsertIndexDataCommand> insertIndexDataCommands) {
        List<IndexData> indexData = insertIndexDataCommands.stream()
                .map(InsertIndexDataCommand::toIndexData)
                .toList();
        return indexDataRepository.saveAll(indexData);
    }
}
