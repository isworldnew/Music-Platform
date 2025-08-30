package ru.smirnov.musicplatform.mapper.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.tmp.*;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.entity.relation.ArtistsSocialNetworks;
import ru.smirnov.musicplatform.mapper.abstraction.ArtistMapper;
import ru.smirnov.musicplatform.mapper.abstraction.ArtistSocialNetworkMapper;
import ru.smirnov.musicplatform.mapper.abstraction.DistributorByArtistMapper;

import java.util.List;

@Component
public class ArtistMapperImplementation implements ArtistMapper {

    private final ArtistSocialNetworkMapper artistSocialNetworkMapper;
    private final DistributorByArtistMapper distributorByArtistMapper;

    @Autowired
    public ArtistMapperImplementation(
            ArtistSocialNetworkMapper artistSocialNetworkMapper,
            DistributorByArtistMapper distributorByArtistMapper
    ) {
        this.artistSocialNetworkMapper = artistSocialNetworkMapper;
        this.distributorByArtistMapper = distributorByArtistMapper;
    }

    @Override
    public Artist artistRequestToArtistEntity(ArtistRequest dto) {
        Artist artist = new Artist();
        artist.setName(dto.getName());
        artist.setDescription(dto.getDescription());
        return artist;
    }

    @Override
    public ArtistResponse artistEntityToArtistResponse(Artist artist) {
        ArtistResponse dto = new ArtistResponse();
        dto.setId(artist.getId());
        dto.setName(artist.getName());
        dto.setDescription(artist.getDescription());
        dto.setCoverReference(artist.getImageReference());

        List<ArtistSocialNetworkResponse> socialNetworks = artist.getSocialNetworks().stream()
                .map(socialNetwork -> this.artistSocialNetworkMapper.artistSocialNetworkEntityToArtistSocialNetworkResponse(socialNetwork))
                .toList();

        dto.setSocialNetworks(socialNetworks);

        return dto;
    }

    @Override
    public ExtendedArtistResponse artistEntityToExtendedArtistResponse(Artist artist) {
        ExtendedArtistResponse dto = new ExtendedArtistResponse();
        dto.setId(artist.getId());
        dto.setName(artist.getName());
        dto.setDescription(artist.getDescription());
        dto.setCoverReference(artist.getImageReference());

        List<ArtistSocialNetworkResponse> socialNetworks = artist.getSocialNetworks().stream()
                .map(socialNetwork -> this.artistSocialNetworkMapper.artistSocialNetworkEntityToArtistSocialNetworkResponse(socialNetwork))
                .toList();

        dto.setSocialNetworks(socialNetworks);

        List<DistributorByArtistResponse> distributors = artist.getRelationWithDistributors().stream()
                .map(relation-> this.distributorByArtistMapper.distributorEntityToDistributorByArtistResponse(
                        relation.getId(), relation.getStatus(), relation.getDistributor())
                )
                .toList();

        dto.setDistributors(distributors);

        return dto;
    }

    @Override
    public ArtistShortcutResponse artistEntityToArtistShortcutResponse(Artist artist) {
        ArtistShortcutResponse dto = new ArtistShortcutResponse();
        dto.setId(artist.getId());
        dto.setName(artist.getName());
        return dto;
    }

}
