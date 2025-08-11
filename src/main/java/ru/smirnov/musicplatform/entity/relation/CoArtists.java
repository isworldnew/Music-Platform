package ru.smirnov.musicplatform.entity.relation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.entity.domain.Track;

@Entity
@Table(name = "co_artists")
@Data
public class CoArtists {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "track_id")
    @JsonBackReference
    private Track track;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    @JsonBackReference
    private Artist artist;

}
