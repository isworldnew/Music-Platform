package ru.smirnov.musicplatform.temporary;

import ru.smirnov.musicplatform.dto.tmp.ArtistRequest;
import ru.smirnov.musicplatform.entity.domain.Artist;

public interface ArtistMapper {

    Artist artistRequestToArtistEntity(ArtistRequest dto);

}
