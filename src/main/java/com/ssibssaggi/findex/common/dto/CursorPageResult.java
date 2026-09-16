package com.ssibssaggi.findex.common.dto;

import java.util.List;
import java.util.function.Function;

public record CursorPageResult<T>(
        List<T> content,
        String nextCursor,
        Long nextIdAfter,
        Integer size,
        Long totalElements,
        Boolean hasNext
) {
    public static <T> CursorPageResult<T> of(List<T> content, PageMeta pageMeta) {
        return new CursorPageResult<>(content,
                pageMeta.nextCursor(),
                pageMeta.nextIdAfter(),
                pageMeta.size(),
                pageMeta.totalElements(),
                pageMeta.hasNext());
    }

    public <R> CursorPageResult<R> map(Function<T, R> mapper) {
        List<R> mapperContent = content.stream().map(mapper).toList();
        return new CursorPageResult<>(mapperContent,
                this.nextCursor(),
                this.nextIdAfter(),
                this.size(),
                this.totalElements(),
                this.hasNext()
        );
    }
}
