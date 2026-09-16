package com.ssibssaggi.findex.common.exception;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * API 에러 응답의 공통 포맷.
 *
 * <p>예외 발생 시점, HTTP 상태 코드, 메시지, 부가 설명을 포함하며
 * {@link GlobalExceptionHandler}에서 생성되어 클라이언트에 반환된다.</p>
 *
 * @param timestamp 에러 발생 시각 (UTC 기준)
 * @param status    HTTP 상태 코드
 * @param message   에러 메시지
 * @param details   에러에 대한 부가 설명 (없을 경우 {@code null})
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        Instant timestamp,
        Integer status,
        String message,
        String details
) {
    public static ErrorResponse of(Integer status, String message, String details) {
        return new ErrorResponse(Instant.now(), status, message, details);
    }
}
