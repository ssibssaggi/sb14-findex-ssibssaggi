package com.ssibssaggi.findex.domain.support;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IndexDataCalculator {

    // 전일/기준 대비 증감분(현재값 - 이전값) 계산
    public static BigDecimal calculateVersus(
            BigDecimal currentPrice,
            BigDecimal previousPrice
    ) {
        return currentPrice.subtract(previousPrice);
    }

    public static BigDecimal calculateRate(BigDecimal current, BigDecimal before) {
        BigDecimal versus = IndexDataCalculator.calculateVersus(current, before);

        if (before.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return versus
                .divide(before, 5, RoundingMode.HALF_UP)                // 증감분 / 이전값 (소수점 5자리까지 반올림하여 정밀도 확보)
                .multiply(BigDecimal.valueOf(100))                            // 비율을 퍼센트(%) 단위로 변환
                .setScale(2, RoundingMode.HALF_UP);                  // 최종 결과는 소수점 2자리로 반올림
    }
}
