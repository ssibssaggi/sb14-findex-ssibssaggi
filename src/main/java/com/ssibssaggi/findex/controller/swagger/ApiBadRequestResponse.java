package com.ssibssaggi.findex.controller.swagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ssibssaggi.findex.common.exception.ErrorResponse;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * ErrorResponse 관련 Swagger 문서는 모든 컨트롤러 영역에서 발생하여 문서 어노테이션 정의하여 재사용(다른 어노테이션 동일)
 *
 * @Target : 메서드에만 붙일 수 있음을 명시
 * @Retention : springdoc이 애플리케이션 실행 중에 사용되야하니까 RUNTIME 적용
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(
        responseCode = "400",
        description = "잘못된 요청",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
)
public @interface ApiBadRequestResponse {
}
