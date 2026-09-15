package com.ssibssaggi.findex.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.ssibssaggi.findex.controller.dto.CursorPaginationCondition;
import com.ssibssaggi.findex.controller.dto.IndexDataFilterCondition;
import com.ssibssaggi.findex.domain.entity.index.IndexData;
import com.ssibssaggi.findex.domain.entity.index.IndexInformation;
import com.ssibssaggi.findex.domain.entity.index.PeriodType;
import com.ssibssaggi.findex.domain.support.IndexInfoTargetDate;

public interface IndexDataCustomRepository {

    Optional<LocalDate> findTargetDate(LocalDate today, Long indexInfoId);

    List<IndexData> findAllByIndexInfoIdAndDateBetween(
            Long indexInfoId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<IndexInfoTargetDate> findTargetDates(LocalDate baseDate, List<Long> indexInfoIds);

    /**
     * 특정 기준일의 지수 데이터를 종가 내림차순으로 조회한다.
     *
     * @param baseDate    기준일
     * @param indexInfoId 지수 정보 FK (null이면 전체 지수)
     * @param limit       최대 조회 건수
     */
    List<IndexData> findByBaseDate(
            LocalDate baseDate,
            Long indexInfoId,
            Integer limit
    );

    /**
     * 특정 기준일을 기준으로 기간별 지수 데이터를 조회한다.
     *
     * @param baseDate    기준일
     * @param indexInfoId 지수 정보 FK (null이면 전체 지수)
     * @param periodType  조회 기간 유형
     * @param limit       최대 조회 건수
     */
    List<IndexData> findByBaseDateAndPeriod(
            LocalDate baseDate,
            Long indexInfoId,
            PeriodType periodType,
            Integer limit
    );

    /**
     * 특정 지수의 특정 기준일 데이터를 조회한다.
     *
     * @param baseDate    기준일
     * @param indexInfoId 지수 정보 FK
     */
    List<IndexData> findAllByBaseDateAndIndexInfoId(
            LocalDate baseDate,
            Long indexInfoId
    );

    List<IndexData> searchIndexDatas(
            IndexDataFilterCondition indexDataFilterCondition,
            CursorPaginationCondition cursorPaginationCondition
    );

    Optional<LocalDate> findLatestBaseDate(IndexInformation indexInformation);

    List<IndexData> findByIndexInformationIdAndBaseDateBetween(
            Long indexInformationId, LocalDate startDate, LocalDate endDate, String sortField, String sortDirection
    );

    Long count(IndexDataFilterCondition searchCondition);
}
