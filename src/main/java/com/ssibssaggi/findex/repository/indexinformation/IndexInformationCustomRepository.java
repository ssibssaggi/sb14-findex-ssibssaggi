package com.ssibssaggi.findex.repository.indexinformation;

import java.util.List;

import com.ssibssaggi.findex.controller.dto.CursorPaginationCondition;
import com.ssibssaggi.findex.controller.dto.IndexInfoFilterCondition;
import com.ssibssaggi.findex.domain.entity.index.IndexInformation;

public interface IndexInformationCustomRepository {
    List<IndexInformation> searchIndexInfos(
            IndexInfoFilterCondition indexInfoFilterCondition,
            CursorPaginationCondition cursorPaginationCondition
    );

    Long count(IndexInfoFilterCondition searchCondition);

    List<IndexInformation> findByAutoSyncConfigEnabled(
            CursorPaginationCondition paginationCondition,
            Long indexInfoId,
            Boolean enabled
    );

    Long countByEnabled(Long id, Boolean enabled);

    List<Long> findIdsByFavoriteTrue();
}
