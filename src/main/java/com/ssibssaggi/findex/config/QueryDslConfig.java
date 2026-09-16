package com.ssibssaggi.findex.config;

import jakarta.persistence.EntityManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.querydsl.jpa.impl.JPAQueryFactory;

@Configuration // QueryDslConfig는 설정을 위한 클래스
@EnableJpaAuditing // 값을 자동으로 등록해주는 JPA 어노테이션
public class QueryDslConfig {

    // 프록시 객체 ("매 호출마다 진짜 EntityManager를 찾아서 위임")
    //    @PersistenceContext
    //    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
