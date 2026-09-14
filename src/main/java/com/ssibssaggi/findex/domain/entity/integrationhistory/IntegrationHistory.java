package com.ssibssaggi.findex.domain.entity.integrationhistory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.ssibssaggi.findex.domain.entity.index.IndexInformation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IntegrationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private JobType jobtype;
    private LocalDate targetDate;
    private String worker;
    private LocalDateTime jobTime;
    private IntegrationResult result;

    @ManyToOne
    @JoinColumn(name = "index_information_id")
    private IndexInformation indexInformation;

    public static IntegrationHistory createIndexInformationHistory(
            String worker,
            IndexInformation indexInformation) {
        return new IntegrationHistory(
                null,
                JobType.INDEX_INFO,
                null,
                worker,
                LocalDateTime.now(ZoneId.systemDefault()),
                IntegrationResult.SUCCESS,
                indexInformation
        );
    }

    public static IntegrationHistory createIndexDataHistory(
            String worker,
            LocalDate targetDate,
            IndexInformation indexInformation
    ) {
        return new IntegrationHistory(
                null,
                JobType.INDEX_DATA,
                targetDate,
                worker,
                LocalDateTime.now(ZoneId.systemDefault()),
                IntegrationResult.SUCCESS,
                indexInformation
        );
    }
}