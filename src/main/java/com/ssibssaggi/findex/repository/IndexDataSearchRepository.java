package com.ssibssaggi.findex.repository;

import java.util.List;

import com.ssibssaggi.findex.controller.dto.CursorPaginationCondition;
import com.ssibssaggi.findex.controller.dto.IndexDataFilterCondition;
import com.ssibssaggi.findex.domain.entity.index.IndexData;

public interface IndexDataSearchRepository {
    List<IndexData> searchIndexDatas(
            IndexDataFilterCondition indexDataFilterCondition,
            CursorPaginationCondition cursorPaginationCondition
    );

    Long count(IndexDataFilterCondition searchCondition);
}
