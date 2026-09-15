package com.ssibssaggi.findex.domain.entity.index;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @ToString.Exclude
    @OneToOne(
            mappedBy = "indexInformation",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private AutoSyncConfig autoSyncConfig;

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
        entity.initAutoSyncConfig();
        return entity;
    }

    public static IndexInformation createWithOpenApi(
            String indexClassification,
            String indexName,
            Integer employedItemsCount,
            LocalDate basePointInTime,
            Float baseIndex
    ) {
        IndexInformation entity = new IndexInformation();
        entity.indexClassification = indexClassification;
        entity.indexName = indexName;
        entity.employedItemsCount = employedItemsCount;
        entity.basePointInTime = basePointInTime;
        entity.baseIndex = baseIndex;
        entity.sourceType = SourceType.OPEN_API;
        entity.favorite = false;
        entity.initAutoSyncConfig();
        return entity;
    }

    private void initAutoSyncConfig() {
        this.autoSyncConfig = AutoSyncConfig.create(this);
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
        this.sourceType = SourceType.USER;
    }

    public void updateWithOpenApi(
            Integer employedItemsCount,
            LocalDate basePointInTime,
            Float baseIndex
    ) {
        this.employedItemsCount = employedItemsCount;
        this.basePointInTime = basePointInTime;
        this.baseIndex = baseIndex;
        this.sourceType = SourceType.OPEN_API;
    }

    public IndexInformation updateEnabled(boolean enabled) {
        autoSyncConfig.updateEnabled(enabled);
        return this;
    }

    public boolean isAutoSyncEnabled() {
        return autoSyncConfig.isEnabled();
    }

    public Long getAutoSyncId() {
        return autoSyncConfig.getId();
    }

    public boolean isDeletable() {
        return this.sourceType != SourceType.OPEN_API;
    }
}
