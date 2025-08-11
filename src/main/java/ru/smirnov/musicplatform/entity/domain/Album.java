package ru.smirnov.musicplatform.entity.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import ru.smirnov.musicplatform.entity.auxiliary.hierarchy.OfficialMusicCollection;
import ru.smirnov.musicplatform.entity.relation.SavedAlbums;
import ru.smirnov.musicplatform.entity.relation.TracksByAlbums;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "albums")
@Data
public class Album extends OfficialMusicCollection {

    @ManyToOne
    @JoinColumn(name = "artist_id")
    @JsonBackReference
    private Artist artist;

    @OneToMany(mappedBy = "album")
    @JsonManagedReference
    private List<TracksByAlbums> tracks = new ArrayList<>();

    @OneToMany(mappedBy = "album")
    @JsonManagedReference
    private List<SavedAlbums> savedBy = new ArrayList<>();

}
