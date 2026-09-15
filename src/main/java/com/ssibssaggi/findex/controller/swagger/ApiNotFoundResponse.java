package com.ssibssaggi.findex.controller.swagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

import com.ssibssaggi.findex.common.exception.ErrorResponse;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(
        responseCode = "404",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
)
public @interface ApiNotFoundResponse {

    @AliasFor(annotation = ApiResponse.class, attribute = "description")
    String description() default "요청한 리소스를 찾을 수 없음";
}
