package com.ssibssaggi.findex.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IndexDataCustomRepositoryImpl implements IndexDataCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
}
