package ru.smirnov.musicplatform.entity.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.entity.auxiliary.hierarchy.MusicCollection;
import ru.smirnov.musicplatform.entity.relation.SavedPlaylists;
import ru.smirnov.musicplatform.entity.relation.TracksByPlaylists;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "playlists")
@Data
public class Playlist extends MusicCollection {

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "playlist")
    @JsonManagedReference
    private List<TracksByPlaylists> tracks = new ArrayList<>();

    @OneToMany(mappedBy = "playlist")
    @JsonManagedReference
    private List<SavedPlaylists> savedBy = new ArrayList<>();

}
