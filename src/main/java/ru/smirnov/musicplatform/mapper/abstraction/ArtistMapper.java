package ru.smirnov.musicplatform.mapper.abstraction;

import ru.smirnov.musicplatform.dto.domain.artist.ArtistRequest;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistResponse;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistShortcutResponse;
import ru.smirnov.musicplatform.dto.domain.artist.ExtendedArtistResponse;
import ru.smirnov.musicplatform.entity.domain.Artist;

public interface ArtistMapper {

    Artist artistRequestToArtistEntity(ArtistRequest dto);

    ArtistResponse artistEntityToArtistResponse(Artist entity);

    ExtendedArtistResponse artistEntityToExtendedArtistResponse(Artist artist);

    ArtistShortcutResponse artistEntityToArtistShortcutResponse(Artist artist);
}
