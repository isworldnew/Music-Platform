package ru.smirnov.musicplatform.entity.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import ru.smirnov.musicplatform.entity.auxiliary.enums.TrackStatus;
import ru.smirnov.musicplatform.entity.relation.*;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tracks")
@Data
public class Track {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String name;

//    @Column(columnDefinition = "INTERVAL", nullable = false)
//    private Duration duration;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String genre;

    @Column(name = "number_of_plays", columnDefinition = "BIGINT DEFAULT 0", nullable = false)
    private Long numberOfPlays = 0L;

    @Column(name = "upload_date_time", columnDefinition = "TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private OffsetDateTime uploadDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrackStatus status;

    @Column(name = "image_reference", columnDefinition = "TEXT")
    private String imageReference;

    @Column(name = "audiofile_reference", columnDefinition = "TEXT", nullable = false)
    private String audiofileReference;

    // так... ну трек у меня не удаляется, он упраялется через статус

    @ManyToOne
    @JoinColumn(name = "artist_id")
    @JsonBackReference
    private Artist artist;

    @OneToMany(mappedBy = "track")
    @JsonManagedReference
    private List<CoArtists> coArtists = new ArrayList<>();

    @OneToMany(mappedBy = "track")
    @JsonManagedReference
    private List<TracksByAlbums> albums = new ArrayList<>();

    @OneToMany(mappedBy = "track")
    @JsonManagedReference
    private List<TracksByCharts> charts = new ArrayList<>();

    @OneToMany(mappedBy = "track")
    @JsonManagedReference
    private List<TracksByPlaylists> playlists = new ArrayList<>();

    @OneToMany(mappedBy = "track")
    @JsonManagedReference
    private List<SavedTracks> savedBy = new ArrayList<>();

    @OneToMany(mappedBy = "track")
    @JsonManagedReference
    private List<TaggedTracks> tags = new ArrayList<>();
}
