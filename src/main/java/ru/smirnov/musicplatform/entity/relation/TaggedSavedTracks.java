/*package ru.smirnov.musicplatform.entity.relation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import ru.smirnov.musicplatform.entity.domain.Tag;

@Entity
@Table(name = "tagged_saved_tracks")
@Data
public class TaggedSavedTracks {

    // и тут я задумался... Может нужна была просто таблица, связывающая теги и треки?
    // просто в бизнес-логике проверять (когда пользователь вешает тег на трек), что
    // данный трек у него сохранён

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "saved_track_id")
    @JsonBackReference
    private SavedTracks savedTrack;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    @JsonBackReference
    private Tag tag;

}*/
