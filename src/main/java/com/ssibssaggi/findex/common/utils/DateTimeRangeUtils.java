package com.ssibssaggi.findex.common.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeRangeUtils {
    public static LocalDateTime toStartOfDay(LocalDateTime datetime) {
        if (Objects.isNull(datetime)) {
            return null;
        }
        return datetime.with(LocalTime.MIDNIGHT);
    }

    public static LocalDateTime toLastOfDay(LocalDateTime dateTime) {
        if (Objects.isNull(dateTime)) {
            return null;
        }
        return dateTime.plusDays(1L).with(LocalTime.MIDNIGHT).minusSeconds(1L);
    }
}
