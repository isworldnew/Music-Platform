package ru.smirnov.musicplatform.entity.auxiliary.hierarchy;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import ru.smirnov.musicplatform.entity.auxiliary.enums.OfficialMusicCollectionStatus;

@MappedSuperclass @Data
public abstract class OfficialMusicCollection extends MusicCollection {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OfficialMusicCollectionStatus status;

}
