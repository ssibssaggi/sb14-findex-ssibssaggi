package com.ssibssaggi.findex.service;

import com.ssibssaggi.findex.application.index.dto.IndexDataCreateCommand;
import com.ssibssaggi.findex.application.index.dto.IndexDataUpdateCommand;
import com.ssibssaggi.findex.client.openapi.dto.indexdata.IndexDataFetchResult;
import com.ssibssaggi.findex.common.exception.CustomException;
import com.ssibssaggi.findex.domain.entity.index.IndexData;
import com.ssibssaggi.findex.domain.entity.index.IndexInformation;
import com.ssibssaggi.findex.repository.IndexDataRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndexDataService {

    private final IndexDataRepository dataRepository;

    //IndexInformationService를 참고하여 OpenApi 메서드 제작
    public List<IndexData> upsertDataByIndexInformationAndBaseDate(
        List<IndexDataFetchResult> commands) {
        List<IndexData> indexDatas = commands.stream()
            .map(this::upsertData)
            .toList();

        return dataRepository.saveAll(indexDatas);
    }

    private IndexData upsertData(IndexDataFetchResult command) {
        return dataRepository.findByIndexInformationAndBaseDate(
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


    public IndexData createData(IndexDataCreateCommand createCommand,
        IndexInformation indexInformation) {
        Long indexInfoId = createCommand.indexInfoId();
        LocalDate baseDate = createCommand.baseDate();

        Boolean isDuplicate = dataRepository.existsByIndexInformationAndBaseDate(
            indexInformation, baseDate);
        if (isDuplicate) {
            throw new CustomException("잘못된 요청입니다.", HttpStatus.BAD_REQUEST,
                "지수 정보 id: " + indexInfoId + "번 - 정보가 존재하지 않습니다.");
        }

        IndexData indexData = createCommand.toEntity(indexInformation);
        return dataRepository.save(indexData);
    }

    public IndexData findById(Long id) {
        return dataRepository.findById(id)
            .orElseThrow(() -> new CustomException("잘못된 요청입니다.",
                HttpStatus.NOT_FOUND,
                "지수 데이터 id: " + id + "번 - 정보가 존재하지 않습니다."));
    }

    public IndexData update(
        Long id, IndexDataUpdateCommand updateCommand
    ) {
        IndexData indexData = dataRepository.findById(id)
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

    public void delete(Long id) {
        IndexData indexData = dataRepository.findById(id)
            .orElseThrow(() -> new CustomException("잘못된 요청입니다.",
                HttpStatus.NOT_FOUND,
                "지수 데이터 id: " + id + "번 - 정보가 존재하지 않습니다."));
        dataRepository.delete(indexData);
    }

    public void deleteByIndexInfoId(Long indexInfoId) {
        dataRepository.deleteByIndexInformationId(indexInfoId);
    }
}