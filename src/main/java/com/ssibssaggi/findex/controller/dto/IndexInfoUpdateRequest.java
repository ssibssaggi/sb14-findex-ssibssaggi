package com.ssibssaggi.findex.controller.dto;

import com.ssibssaggi.findex.application.index.dto.IndexInfoUpdateCommand;

public record IndexInfoUpdateRequest(
        Integer employedItemsCount,
        String basePointInTime,
        Float baseIndex,
        Boolean favorite
) {
    public IndexInfoUpdateCommand toCommand() {
        return new IndexInfoUpdateCommand(
                employedItemsCount,
                basePointInTime,
                baseIndex,
                favorite
        );
    }
}