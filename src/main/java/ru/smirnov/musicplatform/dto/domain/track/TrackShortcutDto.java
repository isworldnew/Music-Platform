package ru.smirnov.musicplatform.dto.domain.track;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistShortcutDto;

import java.util.List;

@Data @NoArgsConstructor
public class TrackShortcutDto {

    private Long id;

    private String name;

    private String status;

    private String coverReference;

    private ArtistShortcutDto artist;

    private List<ArtistShortcutDto> coArtists;

}
