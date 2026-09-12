package com.ssibssaggi.findex.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IndexDataCustomRepository implements IndexDataCustomRepositoryImpl {
    private final JPAQueryFactory jpaQueryFactory;
}
