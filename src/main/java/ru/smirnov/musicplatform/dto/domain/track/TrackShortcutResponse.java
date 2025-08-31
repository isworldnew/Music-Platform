package ru.smirnov.musicplatform.dto.domain.track;

import lombok.Data;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistShortcutResponse;

import java.util.List;

@Data
public class TrackShortcutResponse {

    private Long id;

    private String name;

    private String coverReference;

    private String audioReference;

    private String genre;

    private String status;

    private boolean isSaved; // для пользователя

    private ArtistShortcutResponse artist;

    private List<ArtistShortcutResponse> coArtists;

}
