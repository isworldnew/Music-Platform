package ru.smirnov.musicplatform.dto.tmp;

import lombok.Data;

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

    private String status;

    private ArtistShortcutResponse artist;

    private List<ArtistShortcutResponse> coArtists;

}
