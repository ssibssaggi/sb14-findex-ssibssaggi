package com.ssibssaggi.findex.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * {@link CustomException}을 처리하여 예외에 지정된 상태 코드와 메시지, 상세 설명을 담은 에러 응답을 반환한다.
     *
     * @param e 발생한 CustomException
     * @return 예외 정보를 담은 ErrorResponse와 해당 HTTP 상태 코드
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(e.getStatus())
                .body(ErrorResponse.of(e.getStatus().value(), e.getMessage(), e.getDetail()));
    }

    /**
     * {@link CustomException}으로 명시적으로 처리되지 않은 그 외 모든 예외를 처리하여 500(Internal Server Error) 응답을 반환한다.
     *
     * @param e 발생한 예외
     * @return 500 상태 코드와 에러 메시지를 담은 ErrorResponse
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
    }
}
