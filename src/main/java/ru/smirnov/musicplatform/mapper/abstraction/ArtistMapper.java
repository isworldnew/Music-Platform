package ru.smirnov.musicplatform.mapper.abstraction;

import ru.smirnov.musicplatform.dto.domain.artist.ArtistExtendedResponse;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistRequest;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistResponse;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistShortcutResponse;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;
import ru.smirnov.musicplatform.projection.abstraction.TrackShortcutProjection;

import java.util.List;

public interface ArtistMapper {

    Artist artistRequestToArtistEntity(ArtistRequest dto);

    ArtistResponse artistEntityToArtistResponse(
            Artist artist,
            List<MusicCollectionShortcutProjection> albums,
            List<TrackShortcutProjection> tracks
    );

    ArtistExtendedResponse artistEntityToArtistExtendedResponse(
            Artist artist,
            List<MusicCollectionShortcutProjection> albums,
            List<TrackShortcutProjection> tracks
    );

    ArtistShortcutResponse artistEntityToArtistShortcutResponse(Artist artist);
}
