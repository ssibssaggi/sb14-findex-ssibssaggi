package com.ssibssaggi.findex.domain.service;

import com.ssibssaggi.findex.application.index.dto.IndexDataCreateCommand;
import com.ssibssaggi.findex.application.index.dto.IndexDataUpdateCommand;
import com.ssibssaggi.findex.application.indexintegration.InsertIndexDataCommand;
import com.ssibssaggi.findex.client.openapi.dto.indexdata.IndexDataFetchResult;
import com.ssibssaggi.findex.controller.dto.IndexDataExportResponse;
import com.ssibssaggi.findex.domain.entity.index.IndexData;
import com.ssibssaggi.findex.domain.entity.index.IndexInformation;
import java.time.LocalDate;
import java.util.List;

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

    //IndexInformationService를 참고하여 OpenApi 메서드 제작
    List<IndexData> upsertDataByIndexInformationAndBaseDate(
        List<IndexDataFetchResult> commands);

    IndexData createData(IndexDataCreateCommand createCommand,
        IndexInformation indexInformation);

    IndexData findById(Long id);

    IndexData update(
        Long id, IndexDataUpdateCommand updateCommand
    );

    void delete(Long id);

    void deleteByIndexInfoId(Long indexInfoId);
}
