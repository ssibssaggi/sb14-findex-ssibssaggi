package com.ssibssaggi.findex.domain.entity.index;

import org.springframework.http.HttpStatus;

import com.ssibssaggi.findex.common.exception.CustomException;

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
    MONTHLY("MONTHLY");

    String value;

    public static PeriodType safeValueOf(String value) {
        for (PeriodType period : PeriodType.values()) {
            if (period.value.equalsIgnoreCase(value)) {
                return period;
            }
        }
        throw new CustomException("잘못된 기간 유형 입니다.", HttpStatus.BAD_REQUEST, "현재 입력된 타입값은 [" + value + "] 입니다.");
    }
}
