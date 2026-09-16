package com.ssibssaggi.findex.common.exception;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum ErrorStatus {
    INDEX_DATA_BAD_REQUEST("잘못된 요청입니다.", HttpStatus.BAD_REQUEST.value()),
    INDEX_INFO_DUPLICATE("잘못된 요청입니다.", HttpStatus.BAD_REQUEST.value()),
    INDEX_INFO_NOT_FOUND("잘못된 요청입니다.", HttpStatus.NOT_FOUND.value()),
    INDEX_DATA_DUPLICATE("잘못된 요청입니다.", HttpStatus.BAD_REQUEST.value()),
    INDEX_DATA_NOT_FOUND("잘못된 요청입니다.", HttpStatus.NOT_FOUND.value()),
    INVALID_PERIOD_TYPE("잘못된 기간 유형 입니다.", HttpStatus.BAD_REQUEST.value()),
    CSV_EXPORT_FAILED("잘못된 요청입니다.", HttpStatus.INTERNAL_SERVER_ERROR.value()),
    OPEN_API_CALL_FAILED("OpenAPI 호출 오류", HttpStatus.INTERNAL_SERVER_ERROR.value()),
    INVALID_INDEX_INFO_ID("잘못된 지수정보ID", HttpStatus.BAD_REQUEST.value());
    
    String message;
    Integer status;
}
