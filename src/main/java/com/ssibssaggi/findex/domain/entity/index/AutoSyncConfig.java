package com.ssibssaggi.findex.domain.entity.index;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AutoSyncConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "index_information_id")
    private IndexInformation indexInformation;

    private boolean enabled = false;

    public static AutoSyncConfig create(IndexInformation indexInformation) {
        AutoSyncConfig entity = new AutoSyncConfig();
        entity.indexInformation = indexInformation;
        return entity;
    }

    public void updateEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
