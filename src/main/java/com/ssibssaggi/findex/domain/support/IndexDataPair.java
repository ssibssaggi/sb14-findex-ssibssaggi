package com.ssibssaggi.findex.domain.support;

import java.util.List;

import com.ssibssaggi.findex.domain.entity.index.IndexData;

public record IndexDataPair(
        List<IndexData> baseDateData,
        List<IndexData> beforeDatas

) {
}
