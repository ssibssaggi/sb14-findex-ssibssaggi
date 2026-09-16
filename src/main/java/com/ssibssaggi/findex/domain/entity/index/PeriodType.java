package com.ssibssaggi.findex.domain.entity.index;

import com.ssibssaggi.findex.common.exception.CustomException;
import com.ssibssaggi.findex.common.exception.ErrorStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum PeriodType {
    DAILY("DAILY"),
    WEEKLY("WEEKLY"),
    MONTHLY("MONTHLY"),
    QUARTERLY("QUARTERLY"),
    YEARLY("YEARLY");

    String value;

    public static PeriodType safeValueOf(String value) {
        for (PeriodType period : PeriodType.values()) {
            if (period.value.equalsIgnoreCase(value)) {
                return period;
            }
        }
        throw new CustomException(ErrorStatus.INVALID_PERIOD_TYPE, "현재 입력된 타입값은 [" + value + "] 입니다.");
    }
}
