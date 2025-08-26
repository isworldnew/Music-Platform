package ru.smirnov.musicplatform.entity.auxiliary.hierarchy;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@MappedSuperclass @Data
public abstract class MusicCollectionCreator {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
