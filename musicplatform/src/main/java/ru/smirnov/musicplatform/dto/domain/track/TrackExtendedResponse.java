package ru.smirnov.musicplatform.dto.domain.track;

import lombok.Data;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistShortcutResponse;
import ru.smirnov.musicplatform.dto.domain.tag.TagResponse;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class TrackExtendedResponse {

    private Long id;

    private String name;

    private String coverReference;

    private String audioReference;

    private String genre;

    private Long numberOfPlays;

    private String uploadDateTime;

    private boolean isSaved; // для пользователя

    private String status;

    private ArtistShortcutResponse artist;

    private List<ArtistShortcutResponse> coArtists;

    private List<TagResponse> tags;
}
