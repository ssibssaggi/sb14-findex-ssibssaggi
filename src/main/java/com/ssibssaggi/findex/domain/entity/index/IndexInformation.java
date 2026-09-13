package com.ssibssaggi.findex.domain.entity.index;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"index_classification", "index_name"})
})
public class IndexInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String indexClassification;
    private String indexName;
    private Integer employedItemsCount;
    private LocalDate basePointInTime;
    private Float baseIndex;
    private SourceType sourceType;
    private Boolean favorite;
    private Boolean enabled;

    public static IndexInformation createWithUser(
            String indexName,
            String indexClassification,
            Integer employedItemsCount,
            String basePointInTime,
            Float baseIndex,
            Boolean favorite
    ) {
        IndexInformation entity = new IndexInformation();
        entity.indexName = indexName;
        entity.indexClassification = indexClassification;
        entity.employedItemsCount = employedItemsCount;
        entity.basePointInTime = LocalDate.parse(basePointInTime);
        entity.baseIndex = baseIndex;
        entity.favorite = favorite;
        entity.sourceType = SourceType.USER;
        entity.enabled = false;
        return entity;
    }

    public static IndexInformation createWithOpenApi(
            String indexClassification,
            String indexName,
            Integer employedItemsCount,
            LocalDate basePointInTime,
            Float baseIndex
    ) {
        return new IndexInformation(
                null,
                indexClassification,
                indexName,
                employedItemsCount,
                basePointInTime,
                baseIndex,
                SourceType.OPEN_API,
                false,
                false
        );
    }

    public void updateWithUser(
            Integer employedItemsCount,
            String basePointInTime,
            Float baseIndex,
            Boolean favorite
    ) {
        this.employedItemsCount = employedItemsCount;
        this.basePointInTime = LocalDate.parse(basePointInTime);
        this.baseIndex = baseIndex;
        this.favorite = favorite;
    }

    public void updateWithOpenApi(
            Integer employedItemsCount,
            LocalDate basePointInTime,
            Float baseIndex
    ) {
        this.employedItemsCount = employedItemsCount;
        this.basePointInTime = basePointInTime;
        this.baseIndex = baseIndex;
    }
}
