package com.ssibssaggi.findex.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ssibssaggi.findex.application.index.dto.DataPoints;
import com.ssibssaggi.findex.application.index.dto.IndexDataCreateCommand;
import com.ssibssaggi.findex.application.index.dto.IndexDataUpdateCommand;
import com.ssibssaggi.findex.application.indexintegration.InsertIndexDataCommand;
import com.ssibssaggi.findex.client.openapi.dto.indexdata.IndexDataFetchResult;
import com.ssibssaggi.findex.common.dto.CursorPageResult;
import com.ssibssaggi.findex.common.dto.PageMeta;
import com.ssibssaggi.findex.common.exception.CustomException;
import com.ssibssaggi.findex.controller.dto.CursorPaginationCondition;
import com.ssibssaggi.findex.controller.dto.IndexDataExportDto;
import com.ssibssaggi.findex.controller.dto.IndexDataFilterCondition;
import com.ssibssaggi.findex.domain.entity.index.IndexData;
import com.ssibssaggi.findex.domain.entity.index.IndexInformation;
import com.ssibssaggi.findex.domain.entity.index.PeriodType;
import com.ssibssaggi.findex.repository.IndexDataRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IndexDataServiceImplement implements IndexDataService {

    private final IndexDataRepository indexDataRepository;

    public List<IndexDataExportDto> findAllForExport(Long indexInformationId,
            LocalDate startDate,
            LocalDate endDate) {
        return indexDataRepository
                .findByIndexInformationIdAndBaseDateBetween(indexInformationId, startDate, endDate)
                .stream()
                .map(IndexDataExportDto::from)
                .toList();
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
    public List<IndexData> getIndexDataChartData(Long indexInfoId, String periodType) {
        LocalDate baseDate = LocalDate.now().minusDays(1); // 기준일자(전일)
        Optional<LocalDate> endDate = indexDataRepository.findTargetDate(baseDate, indexInfoId);

        return endDate
                .map(target -> {
                    LocalDate startDate = getTargetDate(
                            target,
                            PeriodType.safeValueOf(periodType)
                    );

                    return indexDataRepository
                            .findAllByIndexInfoIdAndDateBetween(
                                    indexInfoId,
                                    startDate,
                                    target
                            );
                })
                .orElse(List.of());
    }

    @Override
    public List<DataPoints> calculateMovingAverage(List<IndexData> sorted, Integer windowSize) {
        if (sorted.size() < windowSize) {
            return List.of();
        }

        List<DataPoints> result = new ArrayList<>();
        for (int i = sorted.size() - 1; i > windowSize; i--) {
            List<IndexData> window = sorted.subList(i - windowSize + 1, i);
            String currentDate = sorted.get(i).getBaseDate().toString();
            BigDecimal value = window.stream()
                    .map(IndexData::getClosingPrice)           // 각 IndexData → closingPrice만 추출
                    .reduce(BigDecimal.ZERO, BigDecimal::add)  // 다 더함 (0부터 시작해서 누적 합)
                    .divide(new BigDecimal(windowSize),
                            2,
                            RoundingMode.HALF_UP); // windowSize로 나눔, 소수 2자리, 반올림
            System.out.println("value : " + value);
            result.add(DataPoints.of(currentDate, value));
        }

        return result;
    }

    private LocalDate getTargetDate(LocalDate baseDate, PeriodType type) {
        return switch (type) {
            case QUARTERLY -> baseDate.minusMonths(3);
            case YEARLY -> baseDate.minusYears(1);
            default -> baseDate.minusMonths(1);
        };
    }

    @Override
    public CursorPageResult<IndexData> searchDataInfos(
            IndexDataFilterCondition indexDataFilterCondition,
            CursorPaginationCondition cursorPaginationCondition
    ) {

        List<IndexData> entities = indexDataRepository.searchIndexDatas(indexDataFilterCondition,
                cursorPaginationCondition);
        Long totalElements = indexDataRepository.count(indexDataFilterCondition);

        Long nextIdAfter = null;
        String nextCursor = null;
        Boolean hashNext = entities.size() > cursorPaginationCondition.size();

        List<IndexData> content = entities.subList(0,
                Math.min(entities.size(), cursorPaginationCondition.size()));

        if (!entities.isEmpty()) {
            IndexData lastEntity = content.get(content.size() - 1);
            nextIdAfter = lastEntity.getId();
            nextCursor = this.getLastSortValue(cursorPaginationCondition.sortField(), lastEntity);
        }

        PageMeta pageMeta = PageMeta.builder()
                .nextCursor(nextCursor)
                .nextIdAfter(nextIdAfter)
                .size(cursorPaginationCondition.size())
                .totalElements(totalElements)
                .hasNext(hashNext)
                .build();

        return CursorPageResult.of(content, pageMeta);
    }

    private String getLastSortValue(
            String sortField,
            IndexData indexData
    ) {
        return switch (sortField) {
            case "baseDate" -> indexData.getBaseDate().toString();
            case "marketPrice" -> indexData.getMarketPrice().toString();
            case "closingPrice" -> indexData.getClosingPrice().toString();
            case "highPrice" -> indexData.getHighPrice().toString();
            case "lowPrice" -> indexData.getLowPrice().toString();
            case "versus" -> indexData.getVersus().toString();
            case "fluctuation" -> indexData.getFluctuationRate().toString();
            case "tradingQuantity" -> indexData.getTradingQuantity().toString();
            case "tradingPrice" -> indexData.getTradingPrice().toString();
            case "marketTotalAmount" -> indexData.getMarketTotalAmount().toString();
            default -> null;
        };
    }
}