package com.ssibssaggi.findex.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.ssibssaggi.findex.domain.entity.index.IndexData;
import com.ssibssaggi.findex.domain.entity.index.PeriodType;

public interface IndexDataCustomRepository {

    Optional<LocalDate> findTargetDate(LocalDate today, Long indexInfoId);

    public List<IndexData> findByDate(
            LocalDate targetDate,
            Long indexInfoId,
            Integer limit
    );

    List<IndexData> findByDateAndPeriod(
            LocalDate today,
            Long indexInfoId,
            PeriodType periodType,
            Integer limit
    );
}
