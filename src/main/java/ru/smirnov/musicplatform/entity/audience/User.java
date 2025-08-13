package ru.smirnov.musicplatform.entity.audience;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import ru.smirnov.musicplatform.entity.auxiliary.embedding.CommonPersonData;
import ru.smirnov.musicplatform.entity.domain.Playlist;
import ru.smirnov.musicplatform.entity.domain.Tag;
import ru.smirnov.musicplatform.entity.relation.SavedAlbums;
import ru.smirnov.musicplatform.entity.relation.SavedCharts;
import ru.smirnov.musicplatform.entity.relation.SavedPlaylists;
import ru.smirnov.musicplatform.entity.relation.SavedTracks;

import java.sql.Date;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id")
    @JsonBackReference
    private Account account;

    @Embedded
    private CommonPersonData data;

    @Column(name = "date_of_birth", columnDefinition = "DATE", nullable = false)
    private Date dateOfBirth;

    @Column(name = "registration_date", columnDefinition = "TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP", nullable = false)
    @CreationTimestamp
    private OffsetDateTime registrationDate;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Playlist> playlists = new ArrayList<>();


    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<SavedTracks> savedTracks = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<SavedPlaylists> savedPlaylists = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<SavedAlbums> savedAlbums = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<SavedCharts> savedCharts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Tag> tags = new ArrayList<>();

}
