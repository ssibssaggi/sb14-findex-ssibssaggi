package com.ssibssaggi.findex.client.openapi.dto.indexdata;

import java.util.List;

public record IndexDataOpenApiResponse(Response response) {

    public record Response(Body body) {
    }

    public record Body(Items items) {
    }

    public record Items(List<IndexDataOpenApiItem> item) {
    }

    public List<IndexDataOpenApiItem> items() {
        return response().body().items().item();
    }
}
