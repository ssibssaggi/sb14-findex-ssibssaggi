package com.ssibssaggi.findex.application.indexintegration;

import java.time.LocalDate;
import java.util.List;

import com.ssibssaggi.findex.controller.dto.IndexDataSyncRequest;

public record SyncIndexDataCommand(
        List<Long> indexInfosIds,
        LocalDate baseDateFrom,
        LocalDate baseDateTo
) {

    public static SyncIndexDataCommand from(IndexDataSyncRequest request) {
        return new SyncIndexDataCommand(
                request.indexInfoIds(),
                request.baseDateFrom(),
                request.baseDateTo()
        );
    }
}
