package com.ssibssaggi.findex.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class IndexOpenApiConfig {

    @Bean
    public RestClient indexRestClient() {
        return RestClient.builder()
                .baseUrl("https://apis.data.go.kr/1160100/GetMarketIndexInfoService_V2/getStockMarketIndex_V2")
                .build();
    }
}
