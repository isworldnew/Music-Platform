package ru.smirnov.musicplatform.entity.relation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import ru.smirnov.musicplatform.entity.domain.Tag;
import ru.smirnov.musicplatform.entity.domain.Track;

@Entity
@Table(
        name = "tagged_tracks",
        uniqueConstraints = @UniqueConstraint(columnNames = {"track_id", "tag_id"})
)
@Data
public class TaggedTracks {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "track_id")
    @JsonBackReference
    private Track track;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    @JsonBackReference
    private Tag tag;

}
