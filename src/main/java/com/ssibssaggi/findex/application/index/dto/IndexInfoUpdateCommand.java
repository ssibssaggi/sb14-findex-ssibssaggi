package com.ssibssaggi.findex.application.index.dto;

public record IndexInfoUpdateCommand(
        Integer employedItemsCount,
        String basePointInTime,
        Float baseIndex,
        Boolean favorite
) {
}
