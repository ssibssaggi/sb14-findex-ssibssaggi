package com.ssibssaggi.findex.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final HttpStatus status;
    private final String detail;

    /**
     * ErrorStatus에 정의된 상태 코드와 메시지로 CustomException을 생성한다.
     *
     * @param errorStatus 발생한 에러 유형
     */
    public CustomException(ErrorStatus errorStatus) {
        this(errorStatus, null);
    }

    /**
     * ErrorStatus의 상태 코드와 메시지를 사용하되, 상세 설명은 호출부에서 동적으로 지정해 CustomException을 생성한다.
     *
     * @param errorStatus 발생한 에러 유형
     * @param detail      에러에 대한 부가 설명 (예: 식별자, 입력값 등 요청별로 달라지는 내용)
     */
    public CustomException(ErrorStatus errorStatus, String detail) {
        super(errorStatus.getMessage());
        this.status = HttpStatus.valueOf(errorStatus.getStatus());
        this.detail = detail;
    }
}
