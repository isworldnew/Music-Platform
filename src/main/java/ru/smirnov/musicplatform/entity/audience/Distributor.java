package ru.smirnov.musicplatform.entity.audience;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import ru.smirnov.musicplatform.entity.auxiliary.enums.DistributorType;
import ru.smirnov.musicplatform.entity.relation.DistributorsByArtists;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "distributors")
@Data
public class Distributor {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id")
    @JsonBackReference
    private Account account;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "distributor_type", nullable = false)
    private DistributorType distributorType;

    @Column(name = "registration_date", columnDefinition = "TIMESTAMPTZ", nullable = false)
    private OffsetDateTime registrationDate;

    @OneToMany(mappedBy = "distributor")
    @JsonManagedReference
    private List<DistributorsByArtists> artists = new ArrayList<>();

}
