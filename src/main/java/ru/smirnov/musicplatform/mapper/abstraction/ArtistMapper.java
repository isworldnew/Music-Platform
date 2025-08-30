package ru.smirnov.musicplatform.mapper.abstraction;

import ru.smirnov.musicplatform.dto.tmp.ArtistRequest;
import ru.smirnov.musicplatform.dto.tmp.ArtistResponse;
import ru.smirnov.musicplatform.dto.tmp.ArtistShortcutResponse;
import ru.smirnov.musicplatform.dto.tmp.ExtendedArtistResponse;
import ru.smirnov.musicplatform.entity.domain.Artist;

public interface ArtistMapper {

    Artist artistRequestToArtistEntity(ArtistRequest dto);

    ArtistResponse artistEntityToArtistResponse(Artist entity);

    ExtendedArtistResponse artistEntityToExtendedArtistResponse(Artist artist);

    ArtistShortcutResponse artistEntityToArtistShortcutResponse(Artist artist);
}
