package com.ssibssaggi.findex.controller.dto;

import java.time.LocalDate;

public record IndexDataFilterCondition(
        Long indexInfoId,
        LocalDate startDate,
        LocalDate endDate
) {

}
