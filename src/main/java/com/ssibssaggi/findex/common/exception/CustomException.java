package com.ssibssaggi.findex.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final HttpStatus status;
    private final String detail;

    /**
     * CustomException을 생성한다.
     *
     * @param message 예외 메시지 (클라이언트에 노출되는 message 필드로 사용됨)
     * @param status  응답할 HTTP 상태 코드
     * @param detail  에러에 대한 부가 설명
     */
    public CustomException(String message, HttpStatus status, String detail) {
        super(message);
        this.status = status;
        this.detail = detail;
    }
}
