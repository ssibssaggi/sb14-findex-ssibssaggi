package com.ssibssaggi.findex.domain.service.index;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ssibssaggi.findex.application.index.dto.IndexInfoCreateCommand;
import com.ssibssaggi.findex.application.index.dto.IndexInfoUpdateCommand;
import com.ssibssaggi.findex.application.indexintegration.UpsertIndexInformationCommand;
import com.ssibssaggi.findex.common.dto.CursorPageResult;
import com.ssibssaggi.findex.common.dto.PageMeta;
import com.ssibssaggi.findex.common.exception.CustomException;
import com.ssibssaggi.findex.controller.dto.CursorPaginationCondition;
import com.ssibssaggi.findex.controller.dto.IndexInfoFilterCondition;
import com.ssibssaggi.findex.domain.entity.index.IndexInformation;
import com.ssibssaggi.findex.repository.IndexInformationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IndexInformationService {

    private final IndexInformationRepository indexInformationRepository;

    public List<IndexInformation> upsertInformationByIndexClassificationAndIndexName(
            List<UpsertIndexInformationCommand> commands
    ) {
        List<IndexInformation> indexInformations = commands.stream()
                .map(this::upsertInformation)
                .toList();

        return indexInformationRepository.saveAll(indexInformations);
    }

    private IndexInformation upsertInformation(UpsertIndexInformationCommand command) {
        return indexInformationRepository
                .findByIndexClassificationAndIndexName(
                        command.indexClassification(),
                        command.indexName()
                )
                .map(indexInformation -> updateInformation(indexInformation, command))
                .orElseGet(() -> createInformation(command));
    }

    private IndexInformation updateInformation(
            IndexInformation indexInformation,
            UpsertIndexInformationCommand command
    ) {
        indexInformation.updateWithOpenApi(
                command.employedItemCount(),
                command.basePointInTime(),
                command.baseIndex()
        );
        return indexInformation;
    }

    private IndexInformation createInformation(UpsertIndexInformationCommand command) {
        return IndexInformation.createWithOpenApi(
                command.indexClassification(),
                command.indexName(),
                command.employedItemCount(),
                command.basePointInTime(),
                command.baseIndex()
        );
    }

    public IndexInformation createInformation(IndexInfoCreateCommand indexInfoCreateCommand) {
        boolean isDuplicate =
                this.validateByIndexClassificationAndIndexName(indexInfoCreateCommand.indexClassification(),
                        indexInfoCreateCommand.indexName());

        if (isDuplicate) {
            throw new CustomException("잘못된 요청입니다.", HttpStatus.BAD_REQUEST, "동일한 지수 분류와 지수명이 이미 등록되어 있습니다.");
        }

        IndexInformation entity = indexInfoCreateCommand.toEntity();
        return indexInformationRepository.save(entity);
    }

    private boolean validateByIndexClassificationAndIndexName(String indexClassification, String indexName) {
        return indexInformationRepository.existsByIndexClassificationAndIndexName(
                indexClassification,
                indexName
        );
    }

    public IndexInformation findById(Long id) {
        return indexInformationRepository.findById(id)
                .orElseThrow(() -> new CustomException("잘못된 요청입니다.",
                        HttpStatus.NOT_FOUND,
                        "요청 id : " + id + "번 - 정보가 존재하지 않습니다."));
    }

    public void delete(Long id) {
        IndexInformation entity = indexInformationRepository.findById(id)
                .orElseThrow(() -> new CustomException("잘못된 요청입니다.",
                        HttpStatus.NOT_FOUND,
                        "삭제 요청 id : " + id + "번 - 정보가 존재하지 않습니다."));
        indexInformationRepository.delete(entity);
    }

    public IndexInformation update(
            Long id,
            IndexInfoUpdateCommand indexInfoUpdateCommand
    ) {
        IndexInformation entity = indexInformationRepository.findById(id)
                .orElseThrow(() -> new CustomException("잘못된 요청입니다.",
                        HttpStatus.NOT_FOUND,
                        "수정 요청 id : " + id + "번 - 정보가 존재하지 않습니다."));

        entity.updateWithUser(
                indexInfoUpdateCommand.employedItemsCount(),
                indexInfoUpdateCommand.basePointInTime(),
                indexInfoUpdateCommand.baseIndex(),
                indexInfoUpdateCommand.favorite()
        );

        return entity;
    }

    public List<IndexInformation> findSummary() {
        return indexInformationRepository.findAll();
    }

    public CursorPageResult<IndexInformation> searchIndexInfos(
            IndexInfoFilterCondition indexInfoFilterCondition,
            CursorPaginationCondition cursorPaginationCondition
    ) {
        List<IndexInformation> entities = indexInformationRepository.searchIndexInfos(indexInfoFilterCondition,
                cursorPaginationCondition);
        Long totalElements = indexInformationRepository.count(indexInfoFilterCondition);

        Long nextIdAfter = null;
        String nextCursor = null;
        Boolean hashNext = entities.size() > cursorPaginationCondition.size();

        List<IndexInformation> content = entities.subList(0,
                Math.min(entities.size(), cursorPaginationCondition.size()));

        if (!entities.isEmpty()) {
            IndexInformation lastEntity = content.get(content.size() - 1);
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
            IndexInformation indexInformation
    ) {
        return switch (sortField) {
            case "indexClassification" -> indexInformation.getIndexClassification();
            case "indexName" -> indexInformation.getIndexName();
            case "employedItemsCount" -> indexInformation.getEmployedItemsCount().toString();
            default -> null;
        };
    }

    public List<IndexInformation> findAllByIds(List<Long> ids) {
        return indexInformationRepository.findAllById(ids);
    }

    public IndexInformation updateEnabled(Long id, boolean enabled) {
        IndexInformation updating = indexInformationRepository.findById(id)
                .orElseThrow(() -> new CustomException("잘못된 요청입니다.",
                        HttpStatus.NOT_FOUND,
                        "요청 id : " + id + "번 - 정보가 존재하지 않습니다."));

        return updating.updateEnabled(enabled);
    }

    public List<IndexInformation> searchAutoSyncConfig(
            Long indexInfoId,
            Boolean enabled,
            CursorPaginationCondition paginationCondition
    ) {
        return indexInformationRepository.findByAutoSyncConfigEnabled(
                paginationCondition,
                indexInfoId,
                enabled
        );
    }

    public Long countByEnabled(Long id, Boolean enabled) {
        return indexInformationRepository.countByEnabled(id, enabled);
    }

    public List<Long> findFavoriteIndexInfoIds() {
        return indexInformationRepository.findIdsByFavoriteTrue();
    }
}
