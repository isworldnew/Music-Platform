package ru.smirnov.musicplatform.service.implementation.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.relation.ArtistSocialNetworkRequest;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.entity.relation.ArtistsSocialNetworks;
import ru.smirnov.musicplatform.mapper.abstraction.ArtistSocialNetworkMapper;
import ru.smirnov.musicplatform.precondition.abstraction.domain.ArtistPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.relation.ArtistSocialNetworkPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.relation.DistributorByArtistPreconditionService;
import ru.smirnov.musicplatform.repository.relation.ArtistSocialNetworkRepository;
import ru.smirnov.musicplatform.service.abstraction.relation.ArtistSocialNetworkService;

@Service
public class ArtistSocialNetworkServiceImplementation implements ArtistSocialNetworkService {

    private final ArtistSocialNetworkRepository artistSocialNetworkRepository;
    private final ArtistSocialNetworkMapper artistSocialNetworkMapper;
    private final ArtistPreconditionService artistPreconditionService;
    private final DistributorByArtistPreconditionService distributorByArtistPreconditionService;
    private final ArtistSocialNetworkPreconditionService artistSocialNetworkPreconditionService;

    @Autowired
    public ArtistSocialNetworkServiceImplementation(
            ArtistSocialNetworkRepository artistSocialNetworkRepository,
            ArtistSocialNetworkMapper artistSocialNetworkMapper,
            ArtistPreconditionService artistPreconditionService,
            DistributorByArtistPreconditionService distributorByArtistPreconditionService,
            ArtistSocialNetworkPreconditionService artistSocialNetworkPreconditionService
    ) {
        this.artistSocialNetworkRepository = artistSocialNetworkRepository;
        this.artistSocialNetworkMapper = artistSocialNetworkMapper;
        this.artistPreconditionService = artistPreconditionService;
        this.distributorByArtistPreconditionService = distributorByArtistPreconditionService;
        this.artistSocialNetworkPreconditionService = artistSocialNetworkPreconditionService;
    }

    @Override
    public Long addSocialNetwork(Long artistId, ArtistSocialNetworkRequest dto, DataForToken tokenData) {
        Artist artist = this.artistPreconditionService.getByIdIfExists(artistId);
        this.distributorByArtistPreconditionService.checkActiveRelationBetweenDistributorAndArtistExistence(
                tokenData.getEntityId(), artist.getId()
        );
        this.artistSocialNetworkPreconditionService.checkSocialNetworkNameUniqueness(artistId, dto.getName());
        ArtistsSocialNetworks socialNetwork = this.artistSocialNetworkMapper.artistSocialNetworkRequestToArtistSocialNetworkEntity(
                dto, artist
        );

        return this.artistSocialNetworkRepository.save(socialNetwork).getId();
    }

    @Override
    public void updateArtistSocialNetworkReference(Long recordId, String reference, DataForToken tokenData) {
        ArtistsSocialNetworks socialNetwork = this.artistSocialNetworkPreconditionService.getByIdIfExists(recordId);
        this.distributorByArtistPreconditionService.checkActiveRelationBetweenDistributorAndArtistExistence(
                tokenData.getEntityId(), socialNetwork.getArtist().getId()
        );
        socialNetwork.setReference(reference);
        this.artistSocialNetworkRepository.save(socialNetwork);
    }

    @Override
    public void deleteArtistSocialNetwork(Long recordId, DataForToken tokenData) {
        ArtistsSocialNetworks artistsSocialNetworks = this.artistSocialNetworkPreconditionService.getByIdIfExists(recordId);
        this.distributorByArtistPreconditionService.checkActiveRelationBetweenDistributorAndArtistExistence(
                tokenData.getEntityId(), artistsSocialNetworks.getArtist().getId()
        );
        this.artistSocialNetworkRepository.delete(artistsSocialNetworks);
    }
}
