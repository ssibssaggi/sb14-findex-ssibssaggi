package com.ssibssaggi.findex.controller.dto;

public record CursorPaginationCondition(
        Long idAfter,
        String cursor,
        String sortField,
        String sortDirection,
        Integer size
) {
}
