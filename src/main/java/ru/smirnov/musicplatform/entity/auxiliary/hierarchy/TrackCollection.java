package ru.smirnov.musicplatform.entity.auxiliary.hierarchy;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import ru.smirnov.musicplatform.entity.auxiliary.enums.MusicCollectionAccessLevel;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass @Data
public abstract class TrackCollection<CollectionCreator, TrackByCollection, SavedCollection> {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "access_level", columnDefinition = "VARCHAR(255) DEFAULT 'PRIVATE'", nullable = false)
    private MusicCollectionAccessLevel accessLevel = MusicCollectionAccessLevel.PRIVATE;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    @JsonBackReference
    private CollectionCreator creator;

    @OneToMany(mappedBy = "collection")
    @JsonManagedReference
    private List<TrackByCollection> tracks = new ArrayList<>();

    @OneToMany(mappedBy = "collection")
    @JsonManagedReference
    private List<SavedCollection> savedBy = new ArrayList<>();

}
