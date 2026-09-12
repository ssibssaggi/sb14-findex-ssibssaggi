package com.ssibssaggi.findex.application.indexintegration;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ssibssaggi.findex.domain.entity.integrationhistory.IntegrationResult;
import com.ssibssaggi.findex.domain.entity.integrationhistory.JobType;

public record IntegrationHistoryFilterCondition(
        JobType jobType,
        Long indexInfoId,
        LocalDate baseDateFrom,
        LocalDate baseDateTo,
        IntegrationResult status,
        String worker,
        LocalDateTime jobTimeFrom,
        LocalDateTime jobTimeTo
) {
}
