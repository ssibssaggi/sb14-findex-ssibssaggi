package com.ssibssaggi.findex.application.index;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssibssaggi.findex.application.index.dto.IndexInfoCreateCommand;
import com.ssibssaggi.findex.application.index.dto.IndexInfoUpdateCommand;
import com.ssibssaggi.findex.common.dto.CursorPageResult;
import com.ssibssaggi.findex.common.dto.PageMeta;
import com.ssibssaggi.findex.controller.dto.AutoSyncConfigDto;
import com.ssibssaggi.findex.controller.dto.CursorPaginationCondition;
import com.ssibssaggi.findex.controller.dto.IndexInfoFilterCondition;
import com.ssibssaggi.findex.controller.dto.IndexInformationResponse;
import com.ssibssaggi.findex.controller.dto.IndexInformationSummaryResponse;
import com.ssibssaggi.findex.domain.entity.index.IndexInformation;
import com.ssibssaggi.findex.domain.service.IndexDataService;
import com.ssibssaggi.findex.domain.service.index.IndexInformationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IndexInformationApplication {
    private final IndexInformationService indexInformationService;
    private final IndexDataService indexDataService;

    @Transactional
    public IndexInformationResponse saveInformation(IndexInfoCreateCommand indexInfoCreateCommand) {
        IndexInformation savedEntity = indexInformationService.createInformation(indexInfoCreateCommand);

        return IndexInformationResponse.of(savedEntity);
    }

    public IndexInformationResponse findById(Long id) {
        IndexInformation entity = indexInformationService.findById(id);

        return IndexInformationResponse.of(entity);
    }

    @Transactional
    public void deleteById(Long id) {

        IndexInformation information = indexInformationService.findById(id);
        Long infoId = information.getId();
        indexDataService.deleteByIndexInfoId(infoId);
        indexInformationService.delete(information);
    }

    @Transactional
    public IndexInformationResponse update(
            Long id,
            IndexInfoUpdateCommand indexInfoUpdateCommand
    ) {
        IndexInformation entity = indexInformationService.update(id, indexInfoUpdateCommand);
        return IndexInformationResponse.of(entity);
    }

    public List<IndexInformationSummaryResponse> findSummary() {
        return indexInformationService.findSummary().stream()
                .map(IndexInformationSummaryResponse::of)
                .toList();
    }

    public CursorPageResult<IndexInformationResponse> searchIndexInfos(
            IndexInfoFilterCondition indexInfoFilterCondition,
            CursorPaginationCondition cursorPaginationCondition
    ) {

        CursorPageResult<IndexInformation> pageEntities = indexInformationService.searchIndexInfos(
                indexInfoFilterCondition,
                cursorPaginationCondition
        );

        return pageEntities.map(IndexInformationResponse::of);
    }

    @Transactional
    public AutoSyncConfigDto updateAutoSyncConfig(
            Long id,
            boolean enabled
    ) {
        IndexInformation updated = indexInformationService.updateEnabled(id, enabled);
        return AutoSyncConfigDto.of(updated);
    }

    @Transactional
    public CursorPageResult<AutoSyncConfigDto> searchSyncConfig(
            Long indexInfoId,
            Boolean enabled,
            CursorPaginationCondition paginationCondition
    ) {
        List<IndexInformation> found = indexInformationService.searchAutoSyncConfig(
                indexInfoId,
                enabled,
                paginationCondition
        );

        Long totalElements = indexInformationService.countByEnabled(indexInfoId, enabled);

        return createCursorPageResult(found, totalElements, paginationCondition);
    }

    private CursorPageResult<AutoSyncConfigDto> createCursorPageResult(
            List<IndexInformation> found,
            Long totalElement,
            CursorPaginationCondition paginationCondition
    ) {
        boolean hasNext = found.size() > paginationCondition.size();
        List<IndexInformation> contents = found.subList(0,
                Math.min(found.size(), paginationCondition.size()));
        Long nextIdAfter = null;
        String nextCursor = null;

        if (hasNext) {
            IndexInformation lastEntity = contents.get(contents.size() - 1);

            nextIdAfter = lastEntity.getId();
            nextCursor = getLastSortValue(paginationCondition.sortField(), lastEntity);
        }

        PageMeta pageMeta = PageMeta.builder()
                .nextCursor(nextCursor)
                .nextIdAfter(nextIdAfter)
                .size(paginationCondition.size())
                .totalElements(totalElement)
                .hasNext(hasNext)
                .build();

        return CursorPageResult.of(AutoSyncConfigDto.from(contents), pageMeta);
    }

    private String getLastSortValue(String sortField, IndexInformation lastEntity) {
        return switch (sortField) {
            case "enabled" -> Boolean.toString(lastEntity.isAutoSyncEnabled());
            case "indexInfo.indexName" -> lastEntity.getIndexName();
            default -> null;
        };
    }
}
