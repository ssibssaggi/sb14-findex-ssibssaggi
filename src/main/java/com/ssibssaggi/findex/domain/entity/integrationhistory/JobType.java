package com.ssibssaggi.findex.domain.entity.integrationhistory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum JobType {
    INDEX_DATA,
    INDEX_INFO
}