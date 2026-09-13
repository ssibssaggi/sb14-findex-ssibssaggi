package com.ssibssaggi.findex.domain.service;

import com.ssibssaggi.findex.application.index.dto.IndexDataCreateCommand;
import com.ssibssaggi.findex.application.index.dto.IndexDataUpdateCommand;
import com.ssibssaggi.findex.application.indexintegration.InsertIndexDataCommand;
import com.ssibssaggi.findex.client.openapi.dto.indexdata.IndexDataFetchResult;
import com.ssibssaggi.findex.common.exception.CustomException;
import com.ssibssaggi.findex.controller.dto.IndexDataExportResponse;
import com.ssibssaggi.findex.domain.entity.index.IndexData;
import com.ssibssaggi.findex.domain.entity.index.IndexInformation;
import com.ssibssaggi.findex.domain.entity.index.PeriodType;
import com.ssibssaggi.findex.repository.IndexDataRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

    @Override
    public List<IndexData> findPeriodDataByBaseDate(LocalDate baseDate,
        Long indexInfoId,
        String periodType,
        Integer limit) {

        Optional<LocalDate> targetDate = indexDataRepository.findTargetDate(baseDate, indexInfoId);

        return targetDate
            .map(target -> indexDataRepository.findByDateAndPeriod(target,
                indexInfoId,
                PeriodType.safeValueOf(periodType),
                limit)
            ).orElse(List.of());
    }

    @Override
    public List<IndexData> findDataByBaseDate(LocalDate baseDate, Long indexInfoId, Integer limit) {
        Optional<LocalDate> targetDate = indexDataRepository.findTargetDate(baseDate, indexInfoId);

        return targetDate
            .map(target -> indexDataRepository.findByDate(target, indexInfoId, limit))
            .orElse(List.of());
    }


    //IndexInformationService를 참고하여 OpenApi 메서드 제작
    @Override
    public List<IndexData> upsertDataByIndexInformationAndBaseDate(
        List<IndexDataFetchResult> commands) {
        List<IndexData> indexDatas = commands.stream()
            .map(this::upsertData)
            .toList();

        return indexDataRepository.saveAll(indexDatas);
    }

    private IndexData upsertData(IndexDataFetchResult command) {
        return indexDataRepository.findByIndexInformationAndBaseDate(
                command.indexInformation(),
                command.baseDate()
            )
            .map(indexData -> updateData(indexData, command))
            .orElseGet(() -> createData(command));
    }

    private IndexData updateData(IndexData indexData, IndexDataFetchResult command) {
        indexData.update(
            command.marketPrice(),
            command.closingPrice(),
            command.highPrice(),
            command.lowPrice(),
            command.versus(),
            command.fluctuationRate(),
            command.tradingQuantity(),
            command.tradingPrice(),
            command.marketTotalAmount()
        );
        return indexData;
    }

    private IndexData createData(IndexDataFetchResult createCommand) {
        return IndexData.createWithOpenApi(
            createCommand.indexInformation(),
            createCommand.baseDate(),
            createCommand.marketPrice(),
            createCommand.closingPrice(),
            createCommand.highPrice(),
            createCommand.lowPrice(),
            createCommand.versus(),
            createCommand.fluctuationRate(),
            createCommand.tradingQuantity(),
            createCommand.tradingPrice(),
            createCommand.marketTotalAmount()
        );
    }


    @Override
    public IndexData createData(IndexDataCreateCommand createCommand,
        IndexInformation indexInformation) {
        Long indexInfoId = createCommand.indexInfoId();
        LocalDate baseDate = createCommand.baseDate();

        Boolean isDuplicate = indexDataRepository.existsByIndexInformationAndBaseDate(
            indexInformation, baseDate);
        if (isDuplicate) {
            throw new CustomException("잘못된 요청입니다.", HttpStatus.BAD_REQUEST,
                "지수 정보 id: " + indexInfoId + "번 - 정보가 존재하지 않습니다.");
        }

        IndexData indexData = createCommand.toEntity(indexInformation);
        return indexDataRepository.save(indexData);
    }

    @Override
    public IndexData findById(Long id) {
        return indexDataRepository.findById(id)
            .orElseThrow(() -> new CustomException("잘못된 요청입니다.",
                HttpStatus.NOT_FOUND,
                "지수 데이터 id: " + id + "번 - 정보가 존재하지 않습니다."));
    }

    @Override
    public IndexData update(
        Long id, IndexDataUpdateCommand updateCommand
    ) {
        IndexData indexData = indexDataRepository.findById(id)
            .orElseThrow(() -> new CustomException("잘못된 요청입니다.",
                HttpStatus.NOT_FOUND,
                "지수 데이터 id: " + id + "번 - 정보가 존재하지 않습니다."));

        indexData.update(
            updateCommand.marketPrice(),
            updateCommand.closingPrice(),
            updateCommand.highPrice(),
            updateCommand.lowPrice(),
            updateCommand.versus(),
            updateCommand.fluctuationRate(),
            updateCommand.tradingQuantity(),
            updateCommand.tradingPrice(),
            updateCommand.marketTotalAmount()
        );

        return indexData;
    }

    @Override
    public void delete(Long id) {
        IndexData indexData = indexDataRepository.findById(id)
            .orElseThrow(() -> new CustomException("잘못된 요청입니다.",
                HttpStatus.NOT_FOUND,
                "지수 데이터 id: " + id + "번 - 정보가 존재하지 않습니다."));
        indexDataRepository.delete(indexData);
    }

    @Override
    public void deleteByIndexInfoId(Long indexInfoId) {
        indexDataRepository.deleteByIndexInformationId(indexInfoId);
    }
}
