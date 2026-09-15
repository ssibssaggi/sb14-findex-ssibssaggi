package com.ssibssaggi.findex.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssibssaggi.findex.domain.entity.index.IndexData;
import com.ssibssaggi.findex.domain.entity.index.IndexInformation;

//JpaRepository를 상속받음으로서 DB에 저장
public interface IndexDataRepository extends JpaRepository<IndexData, Long>,
        IndexDataCustomRepository {
    //IndexInformation과 BaseDate에 대해 SELECT 쿼리 전송
    Optional<IndexData> findByIndexInformationAndBaseDate(IndexInformation indexInformation,
            LocalDate baseDate);

    //IndexInformation과 BaseDate가 일치하는 데이터 여부 검사
    Boolean existsByIndexInformationAndBaseDate(IndexInformation indexInformation,
            LocalDate baseDate);

    //IndexInformation의 id가 일치하는 데이터 DELETE
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from IndexData i where i.indexInformation.id = :indexInfoId")
    void deleteByIndexInformationId(@Param("indexInfoId") Long indexInfoId);

    List<IndexData> findByIndexInformation_FavoriteTrueOrderByIndexInformation_IdAscBaseDateDesc();

    Optional<IndexData> findFirstByIndexInformation_IdAndBaseDateLessThanOrderByBaseDateDesc(
            Long indexInformationId, LocalDate baseDate);

    Optional<IndexData> findFirstByIndexInformation_IdAndBaseDateLessThanEqualOrderByBaseDateDesc(
            Long indexInformationId, LocalDate baseDate);
}