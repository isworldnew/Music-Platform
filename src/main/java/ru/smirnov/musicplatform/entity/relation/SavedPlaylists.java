package ru.smirnov.musicplatform.entity.relation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.entity.domain.Playlist;

@Entity
@Table(name = "saved_playlists")
@Data
public class SavedPlaylists {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "playlist_id")
    @JsonBackReference
    private Playlist playlist;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

}
