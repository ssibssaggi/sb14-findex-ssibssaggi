package com.ssibssaggi.findex.common.dto;

import lombok.Builder;

@Builder
public record PageMeta(
        String nextCursor,
        Long nextIdAfter,
        Integer size,
        Long totalElements,
        Boolean hasNext
) {
}