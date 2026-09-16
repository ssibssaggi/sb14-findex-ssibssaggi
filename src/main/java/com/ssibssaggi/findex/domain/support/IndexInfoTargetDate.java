package com.ssibssaggi.findex.domain.support;

import java.time.LocalDate;

public record IndexInfoTargetDate(
        Long indexInfoId,
        LocalDate targetDate
) {
}
