package ru.smirnov.musicplatform.entity.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.entity.relation.TaggedTracks;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tags")
@Data
public class Tag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private List<TaggedTracks> tracks = new ArrayList<>();

}
