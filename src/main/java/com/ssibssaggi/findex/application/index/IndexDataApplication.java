package com.ssibssaggi.findex.application.index;

import com.ssibssaggi.findex.application.index.dto.IndexDataCreateCommand;
import com.ssibssaggi.findex.application.index.dto.IndexDataUpdateCommand;
import com.ssibssaggi.findex.controller.dto.IndexDataResponse;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssibssaggi.findex.application.index.dto.Performance;
import com.ssibssaggi.findex.application.index.support.PerformanceAssembler;
import com.ssibssaggi.findex.controller.dto.IndexPerformanceRankResponse;
import com.ssibssaggi.findex.domain.entity.index.IndexData;
import com.ssibssaggi.findex.domain.entity.index.IndexInformation;
import com.ssibssaggi.findex.domain.service.index.IndexInformationService;
import com.ssibssaggi.findex.service.IndexDataService;
import jakarta.transaction.Transactional;
import com.ssibssaggi.findex.domain.service.IndexDataService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndexDataApplication {

    private final IndexDataService indexDataService;
    private final IndexInformationService indexInformationService;

    @Transactional
    public IndexDataResponse saveData(IndexDataCreateCommand createCommand) {
        Long indexInfoId = createCommand.indexInfoId();
        IndexInformation indexInformation = indexInformationService.findById(indexInfoId);
        IndexData savedIndexData = indexDataService.createData(createCommand, indexInformation);

        return IndexDataResponse.toDto(savedIndexData);
    }

    public IndexDataResponse findById(Long id) {
        IndexData indexData = indexDataService.findById(id);

        return IndexDataResponse.toDto(indexData);
    }

    @Transactional
    public IndexDataResponse update(Long id, IndexDataUpdateCommand updateCommand) {
        IndexData indexData = indexDataService.update(id, updateCommand);
        return IndexDataResponse.toDto(indexData);
    }

    @Transactional
    public void delete(Long id) {
        indexDataService.delete(id);
    }

    /*
    @Transactional
    public void deletedByInfoId(Long indexInfoId) {

    }*/
}