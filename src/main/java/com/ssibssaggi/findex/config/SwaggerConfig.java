package com.ssibssaggi.findex.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI findexOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Findex API")
                        .description("가볍고 빠른 외부 API 연동 금융 분석 도구 API 문서")
                        .version("v1"));
    }
}
