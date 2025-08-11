package ru.smirnov.musicplatform.entity.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import ru.smirnov.musicplatform.entity.relation.ArtistsSocialNetworks;
import ru.smirnov.musicplatform.entity.relation.CoArtists;
import ru.smirnov.musicplatform.entity.relation.DistributorsByArtists;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artists")
@Data
public class Artist {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_reference", columnDefinition = "TEXT")
    private String imageReference;


    // может исполнителю тоже сделать статус, мол, он доступен или нет?
    // чтобы можно было управлять тем, появляется его творчество в поиске или нет...

    // bindings

    @OneToMany(
            mappedBy = "artist"
            // вот тут тогда надо будет прописать что-то, если исполнителя можно удалить
    )
    @JsonManagedReference
    private List<ArtistsSocialNetworks> socialNetworks = new ArrayList<>();

    @OneToMany(
            mappedBy = "artist"
            // вот тут тогда надо будет прописать что-то, если исполнителя можно удалить
    )
    @JsonManagedReference
    private List<Track> tracks = new ArrayList<>();

    @OneToMany(
            mappedBy = "artist"
            // вот тут тогда надо будет прописать что-то, если исполнителя можно удалить
    )
    @JsonManagedReference
    private List<Album> albums = new ArrayList<>();

    @OneToMany(
            mappedBy = "artist"
            // вот тут тогда надо будет прописать что-то, если исполнителя можно удалить
    )
    @JsonManagedReference
    private List<CoArtists> coAuthorshipTracks = new ArrayList<>();

    @OneToOne(
            mappedBy = "artist"
            // вот тут тогда надо будет прописать что-то, если исполнителя можно удалить
    )
    @JsonManagedReference
    private DistributorsByArtists relationWithDistributors;

}
