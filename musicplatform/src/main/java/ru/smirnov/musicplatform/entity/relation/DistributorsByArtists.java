package ru.smirnov.musicplatform.entity.relation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import ru.smirnov.musicplatform.entity.audience.Distributor;
import ru.smirnov.musicplatform.entity.auxiliary.enums.DistributionStatus;
import ru.smirnov.musicplatform.entity.domain.Artist;

@Entity
@Table(name = "distributors_by_artists")
@Data
public class DistributorsByArtists {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    @JsonBackReference
    private Artist artist;

    @ManyToOne
    @JoinColumn(name = "distributor_id")
    @JsonBackReference
    private Distributor distributor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DistributionStatus status;

}
