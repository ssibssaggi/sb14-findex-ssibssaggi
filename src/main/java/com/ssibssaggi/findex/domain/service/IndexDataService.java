package com.ssibssaggi.findex.domain.service;

import java.time.LocalDate;
import java.util.List;

import com.ssibssaggi.findex.application.index.dto.DataPoints;
import com.ssibssaggi.findex.application.index.dto.IndexDataCreateCommand;
import com.ssibssaggi.findex.application.index.dto.IndexDataUpdateCommand;
import com.ssibssaggi.findex.application.indexintegration.InsertIndexDataCommand;
import com.ssibssaggi.findex.client.openapi.dto.indexdata.IndexDataFetchResult;
import com.ssibssaggi.findex.common.dto.CursorPageResult;
import com.ssibssaggi.findex.controller.dto.CursorPaginationCondition;
import com.ssibssaggi.findex.controller.dto.IndexDataExportDto;
import com.ssibssaggi.findex.controller.dto.IndexDataFilterCondition;
import com.ssibssaggi.findex.domain.entity.index.IndexData;
import com.ssibssaggi.findex.domain.entity.index.IndexInformation;

public interface IndexDataService {
    List<IndexDataExportDto> findAllForExport(
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

    List<IndexData> getIndexDataChartData(Long indexInfoId, String periodType);

    List<DataPoints> calculateMovingAverage(List<IndexData> sorted, Integer windowSize);

    CursorPageResult<IndexData> searchDataInfos(
            IndexDataFilterCondition indexDataFilterCondition,
            CursorPaginationCondition cursorPaginationCondition
    );

    void deleteByIndexInfoId(Long indexInfoId);
}

