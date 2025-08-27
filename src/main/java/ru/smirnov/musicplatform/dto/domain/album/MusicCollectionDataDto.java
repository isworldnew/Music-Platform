package ru.smirnov.musicplatform.dto.domain.album;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.smirnov.musicplatform.dto.domain.MusicCollectionAuthorDto;
import ru.smirnov.musicplatform.dto.domain.track.TrackShortcutDto;

import java.time.OffsetDateTime;
import java.util.List;

@Data @NoArgsConstructor
public class MusicCollectionDataDto {

    private Long id;

    private MusicCollectionAuthorDto author;

    private String name;

    private String description;

    private String coverReference;

    private OffsetDateTime uploadDateTime;

    private Long numberOfPlays;

    private String status;

    private List<TrackShortcutDto> tracks;

}
