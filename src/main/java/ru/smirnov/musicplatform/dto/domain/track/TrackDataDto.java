package ru.smirnov.musicplatform.dto.domain.track;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistDataDto;
import ru.smirnov.musicplatform.projection.CoArtistProjection;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Data @NoArgsConstructor
public class TrackDataDto {

    private Long id;

    private String name;

    private ArtistData artist;

    private List<CoArtistProjection> coArtists;

    private String genre;

    private String coverReference;

    private String audioReference;

    private Long numberOfPlays;

    private String status;

    private OffsetDateTime uploadDateTime;

    @Data @AllArgsConstructor
    public static class ArtistData {

        private Long id;

        private String name;

    }

}
