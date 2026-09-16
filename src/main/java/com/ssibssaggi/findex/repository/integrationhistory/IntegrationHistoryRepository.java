package com.ssibssaggi.findex.repository.integrationhistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssibssaggi.findex.domain.entity.integrationhistory.IntegrationHistory;

public interface IntegrationHistoryRepository
        extends JpaRepository<IntegrationHistory, Long>, IntegrationHistoryCustomRepository {

    //IndexInformation의 id가 일치하는 데이터 DELETE
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from IntegrationHistory i where i.indexInformation.id = :indexInfoId")
    void deleteByIndexInformationId(@Param("indexInfoId") Long indexInfoId);
}
