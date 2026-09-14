package com.ssibssaggi.findex.application.index.support;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ssibssaggi.findex.application.index.dto.Performance;
import com.ssibssaggi.findex.domain.entity.index.IndexData;
import com.ssibssaggi.findex.domain.support.IndexDataCalculator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PerformanceAssembler {

    public static List<Performance> assemble(
            List<IndexData> baseDateData,
            List<IndexData> periodData
    ) {

        Map<Long, IndexData> baseDataMap = PerformanceAssembler.toMap(baseDateData);
        Map<Long, IndexData> periodDataMap = PerformanceAssembler.toMap(periodData);

        return PerformanceAssembler.toPerformance(baseDataMap, periodDataMap);
    }

    private static Map<Long, IndexData> toMap(List<IndexData> indexData) {
        return indexData.stream()
                .collect(Collectors.toMap(
                        data -> data.getIndexInformation().getId(),
                        data -> data,
                        ((existing, replacement) -> existing)
                ));
    }

    private static List<Performance> toPerformance(
            Map<Long, IndexData> baseData,
            Map<Long, IndexData> periodData
    ) {
        return baseData.values().stream()
                .map(current -> {
                    IndexData before = periodData.get(current.getIndexInformation().getId());

                    if (before == null) {
                        return Performance.withoutComparison(
                                current.getIndexInformation().getId(),
                                current.getIndexInformation().getIndexClassification(),
                                current.getIndexInformation().getIndexName(),
                                current.getClosingPrice()
                        );
                    }

                    BigDecimal currentPrice = current.getClosingPrice();
                    BigDecimal previousPrice = before.getClosingPrice();

                    BigDecimal versus = IndexDataCalculator.calculateVersus(currentPrice, previousPrice);
                    BigDecimal fluctuationRate = IndexDataCalculator.calculateRate(currentPrice, previousPrice);

                    return Performance.of(current.getIndexInformation(),
                            versus,
                            fluctuationRate,
                            currentPrice,
                            previousPrice);
                }).toList();
    }
}
