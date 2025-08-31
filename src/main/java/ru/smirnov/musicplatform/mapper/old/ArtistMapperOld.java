package ru.smirnov.musicplatform.mapper.old;

import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.old.domain.artist.ArtistDataDto;
import ru.smirnov.musicplatform.dto.old.domain.artist.ArtistExtendedDataDto;
import ru.smirnov.musicplatform.dto.old.domain.artist.ArtistShortcutDto;
import ru.smirnov.musicplatform.dto.old.domain.artist.ArtistToCreateDto;
import ru.smirnov.musicplatform.dto.old.relation.ArtistSocialNetworkDto;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.projection.DistributorByArtistProjection;

import java.util.List;
import java.util.Map;

@Component
public class ArtistMapperOld {

    public Artist artistToCreateDtoToArtistEntity(ArtistToCreateDto dto, String coverReference) {
        Artist artist = new Artist();
        artist.setName(dto.getName());
        artist.setDescription(dto.getDescription());
        artist.setImageReference(coverReference);
        return artist;
    }

    public ArtistDataDto createArtistDataDto(Artist artist, Map<String, String> socialNetworks) {
        ArtistDataDto dto = new ArtistDataDto();
        dto.setId(artist.getId());
        dto.setName(artist.getName());
        dto.setDescription(artist.getDescription());
        dto.setCoverReference(artist.getImageReference());
        dto.setSocialNetworks(socialNetworks);
        return dto;
    }

    public ArtistExtendedDataDto artistEntityToArtistExtendedDataDto(
            Artist artist,
            List<ArtistSocialNetworkDto> artistSocialNetworkDtos,
            List<DistributorByArtistProjection> distributorByArtistProjections
    ) {
        ArtistExtendedDataDto dto = new ArtistExtendedDataDto();
        dto.setId(artist.getId());
        dto.setName(artist.getName());
        dto.setDescription(artist.getDescription());
        dto.setCoverReference(artist.getImageReference());
        dto.setSocialNetworks(artistSocialNetworkDtos);
        dto.setDistributors(distributorByArtistProjections);
        return dto;
    }

    public ArtistShortcutDto artistEntityToArtistShortcutDto(Artist artist) {
        ArtistShortcutDto dto = new ArtistShortcutDto();
        dto.setId(artist.getId());
        dto.setName(artist.getName());
        return dto;
    }
}
