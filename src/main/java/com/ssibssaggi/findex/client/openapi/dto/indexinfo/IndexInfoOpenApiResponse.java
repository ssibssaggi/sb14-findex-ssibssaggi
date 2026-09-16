package com.ssibssaggi.findex.client.openapi.dto.indexinfo;

import java.util.List;

public record IndexInfoOpenApiResponse(Response response) {

    public record Response(Body body) {
    }

    public record Body(Items items) {
    }

    public record Items(List<IndexInfoFetchResult> item) {
    }

    public List<IndexInfoFetchResult> toIndexInfoApiItem() {
        return response().body().items().item();
    }
}
