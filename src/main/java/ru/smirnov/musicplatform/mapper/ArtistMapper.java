package ru.smirnov.musicplatform.mapper;

import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.ArtistToCreateDto;
import ru.smirnov.musicplatform.entity.domain.Artist;

@Component
public class ArtistMapper {

    public Artist artistToCreateDtoToArtistEntity(ArtistToCreateDto dto, String coverReference) {
        Artist artist = new Artist();
        artist.setName(dto.getName());
        artist.setDescription(dto.getDescription());
        artist.setImageReference(coverReference);
        return artist;
    }

}
