package ru.smirnov.musicplatform.entity.relation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import ru.smirnov.musicplatform.entity.domain.Playlist;
import ru.smirnov.musicplatform.entity.domain.Track;

@Entity
@Table(
        name = "tracks_by_playlists",
        uniqueConstraints = @UniqueConstraint(columnNames = {"track_id", "playlist_id"})
)
@Data
public class TracksByPlaylists {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "track_id")
    @JsonBackReference
    private Track track;

    @ManyToOne
    @JoinColumn(name = "playlist_id")
    @JsonBackReference
    private Playlist playlist;

}
