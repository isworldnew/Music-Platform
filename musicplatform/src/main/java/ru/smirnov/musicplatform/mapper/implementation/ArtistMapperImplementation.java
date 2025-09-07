package ru.smirnov.musicplatform.mapper.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.domain.artist.*;
import ru.smirnov.musicplatform.dto.relation.DistributorByArtistResponse;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.mapper.abstraction.ArtistMapper;
import ru.smirnov.musicplatform.mapper.abstraction.ArtistSocialNetworkMapper;
import ru.smirnov.musicplatform.mapper.abstraction.DistributorByArtistMapper;
import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;
import ru.smirnov.musicplatform.projection.abstraction.TrackShortcutProjection;

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
    public ArtistResponse artistEntityToArtistResponse(
            Artist artist,
            List<MusicCollectionShortcutProjection> albums,
            List<TrackShortcutProjection> tracks
    ) {
        ArtistResponse dto = new ArtistResponse();
        dto.setId(artist.getId());
        dto.setName(artist.getName());
        dto.setDescription(artist.getDescription());
        dto.setCoverReference(artist.getImageReference());
        dto.setAlbums(albums);
        dto.setTracks(tracks);

        if (artist.getSocialNetworks() != null && !artist.getSocialNetworks().isEmpty()) {
            dto.setSocialNetworks(artist.getSocialNetworks().stream()
                .map(socialNetwork -> this.artistSocialNetworkMapper.artistSocialNetworkEntityToArtistSocialNetworkResponse(socialNetwork))
                .toList()
            );
        }

        return dto;
    }

    @Override
    public ArtistExtendedResponse artistEntityToArtistExtendedResponse(
            Artist artist,
            List<MusicCollectionShortcutProjection> albums,
            List<TrackShortcutProjection> tracks
    ) {
        ArtistExtendedResponse dto = new ArtistExtendedResponse();
        dto.setId(artist.getId());
        dto.setName(artist.getName());
        dto.setDescription(artist.getDescription());
        dto.setCoverReference(artist.getImageReference());
        dto.setAlbums(albums);
        dto.setTracks(tracks);

        if (artist.getSocialNetworks() != null && !artist.getSocialNetworks().isEmpty()) {
            dto.setSocialNetworks(artist.getSocialNetworks().stream()
                    .map(
                            socialNetwork -> this.artistSocialNetworkMapper.artistSocialNetworkEntityToArtistSocialNetworkResponse(socialNetwork)
                    )
                    .toList()
            );
        }

        if (artist.getRelationWithDistributors() != null && !artist.getRelationWithDistributors().isEmpty()) {
            dto.setDistributors(artist.getRelationWithDistributors().stream()
                    .map(relation -> this.distributorByArtistMapper.distributorEntityToDistributorByArtistResponse(
                            relation.getId(),
                            relation.getStatus(),
                            relation.getDistributor()
                    ))
                    .toList()
            );

        }

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
