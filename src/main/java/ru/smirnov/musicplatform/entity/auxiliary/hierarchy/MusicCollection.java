package ru.smirnov.musicplatform.entity.auxiliary.hierarchy;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@MappedSuperclass @Data
public abstract class MusicCollection {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "number_of_plays", columnDefinition = "BIGINT DEFAULT 0", nullable = false)
    private Long numberOfPlays = 0L;

    @Column(name = "image_reference", columnDefinition = "TEXT")
    private String imageReference;

    @Column(name = "upload_date_time", columnDefinition = "TIMESTAMPTZ", nullable = false)
    private OffsetDateTime uploadDateTime;

}
