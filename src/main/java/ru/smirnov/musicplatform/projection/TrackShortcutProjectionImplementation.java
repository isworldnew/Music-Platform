package ru.smirnov.musicplatform.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import ru.smirnov.musicplatform.entity.auxiliary.enums.TrackStatus;

@Data @AllArgsConstructor
public class TrackShortcutProjectionImplementation implements TrackShortcutProjection {

    private Long id;

    private String name;

    private Long artistId;

    private String artistName;

    private TrackStatus status;

    private String imageReference;

    private Boolean saved;

}
