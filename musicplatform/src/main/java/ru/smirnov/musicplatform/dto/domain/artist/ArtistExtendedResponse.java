package ru.smirnov.musicplatform.dto.domain.artist;

import lombok.Data;
import ru.smirnov.musicplatform.dto.relation.DistributorByArtistResponse;
import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;
import ru.smirnov.musicplatform.projection.abstraction.TrackShortcutProjection;

import java.util.List;

@Data
public class ArtistExtendedResponse {

    private Long id;

    private String name;

    private String description;

    private String coverReference;

    private List<ArtistSocialNetworkResponse> socialNetworks;

    private List<TrackShortcutProjection> tracks;

    private List<MusicCollectionShortcutProjection> albums;

    private List<DistributorByArtistResponse> distributors;
}
