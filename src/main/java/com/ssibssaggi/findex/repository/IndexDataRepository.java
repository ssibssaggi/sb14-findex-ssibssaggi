package com.ssibssaggi.findex.repository;

import com.ssibssaggi.findex.domain.entity.index.IndexData;
import com.ssibssaggi.findex.domain.entity.index.IndexInformation;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

//JpaRepository를 상속받음으로서 DB에 저장
public interface IndexDataRepository extends JpaRepository<IndexData, Long>,
    IndexDataCustomRepository {

    //IndexInformation과 BaseDate에 대해 SELECT 쿼리 전송
    Optional<IndexData> findByIndexInformationAndBaseDate(IndexInformation indexInformation,
        LocalDate baseDate);

    List<IndexData> findByIndexInformationIdAndBaseDateBetween(
        Long indexInformationId, LocalDate startDate, LocalDate endDate
    );

    //IndexInformation과 BaseDate가 일치하는 데이터 여부 검사
    Boolean existsByIndexInformationAndBaseDate(IndexInformation indexInformation,
        LocalDate baseDate);

    //IndexInformation의 id가 일치하는 데이터 DELETE
    //삭제된 지수 정보 접근을 방지하기 위해 일괄 벌크 삭제
    @Modifying(clearAutomatically = true)
    void deleteByIndexInformationId(Long indexInfoId);
}