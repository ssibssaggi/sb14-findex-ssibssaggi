package com.ssibssaggi.findex.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssibssaggi.findex.domain.entity.index.IndexInformation;

@Repository
public interface IndexInformationRepository
        extends JpaRepository<IndexInformation, Long>, IndexInformationRepositoryCustom {
    // SELECT * FROM index_information WHERE index_classification = ? AND index_name = ?;
    Optional<IndexInformation> findByIndexClassificationAndIndexName(String indexClassification, String indexName);

    Boolean existsByIndexClassificationAndIndexName(String indexClassification, String indexName);
}

