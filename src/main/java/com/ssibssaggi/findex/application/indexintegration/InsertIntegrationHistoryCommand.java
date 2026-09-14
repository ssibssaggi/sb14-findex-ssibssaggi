package com.ssibssaggi.findex.application.indexintegration;

import java.time.LocalDate;

import com.ssibssaggi.findex.domain.entity.index.IndexInformation;

public record InsertIntegrationHistoryCommand(
        String worker,
        LocalDate targetDate,
        IndexInformation indexInformation
) {

    public static InsertIntegrationHistoryCommand of(
            String worker,
            LocalDate targetDate,
            IndexInformation indexInformation) {
        return new InsertIntegrationHistoryCommand(
                worker,
                targetDate,
                indexInformation
        );
    }
}
