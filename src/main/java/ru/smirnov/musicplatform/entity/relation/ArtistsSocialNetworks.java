package ru.smirnov.musicplatform.entity.relation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import ru.smirnov.musicplatform.entity.domain.Artist;

@Entity
@Table(name = "artists_social_networks")
@Data
public class ArtistsSocialNetworks {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "social_network", columnDefinition = "VARCHAR(255)", nullable = false)
    private String socialNetwork;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String reference;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    @JsonBackReference
    private Artist artist;

}
