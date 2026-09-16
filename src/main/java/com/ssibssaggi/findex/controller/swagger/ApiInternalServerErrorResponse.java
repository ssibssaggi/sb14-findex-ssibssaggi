package com.ssibssaggi.findex.controller.swagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ssibssaggi.findex.common.exception.ErrorResponse;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(
        responseCode = "500",
        description = "서버 내부 오류",
        content = @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                        value = """
                                {
                                  "timestamp": "2025-03-06T05:39:06.152068Z",
                                  "status": 400,
                                  "message": "잘못된 요청입니다.",
                                  "details": "부서 코드는 필수입니다."
                                }
                                """
                )
        )
)
public @interface ApiInternalServerErrorResponse {
}
