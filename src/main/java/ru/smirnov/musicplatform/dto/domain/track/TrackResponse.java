package ru.smirnov.musicplatform.dto.domain.track;

import lombok.Data;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistShortcutResponse;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class TrackResponse {

    private Long id;

    private String name;

    private String coverReference;

    private String audioReference;

    private String genre;

    private Long numberOfPlays;

    private OffsetDateTime uploadDateTime;

    private boolean isSaved; // для пользователя

    private String status;

    private ArtistShortcutResponse artist;

    private List<ArtistShortcutResponse> coArtists;

}
