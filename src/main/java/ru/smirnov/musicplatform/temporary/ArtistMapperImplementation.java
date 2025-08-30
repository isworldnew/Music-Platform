package ru.smirnov.musicplatform.temporary;

import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.tmp.ArtistRequest;
import ru.smirnov.musicplatform.entity.domain.Artist;

@Component
public class ArtistMapperImplementation implements ArtistMapper {

    @Override
    public Artist artistRequestToArtistEntity(ArtistRequest dto) {
        Artist artist = new Artist();
        artist.setName(dto.getName());
        artist.setDescription(dto.getDescription());
        return artist;
    }

}
