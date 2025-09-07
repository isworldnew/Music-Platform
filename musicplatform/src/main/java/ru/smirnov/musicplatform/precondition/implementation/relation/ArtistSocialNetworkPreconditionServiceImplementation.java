package ru.smirnov.musicplatform.precondition.implementation.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.entity.relation.ArtistsSocialNetworks;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.exception.NotFoundException;
import ru.smirnov.musicplatform.precondition.abstraction.relation.ArtistSocialNetworkPreconditionService;
import ru.smirnov.musicplatform.repository.relation.ArtistSocialNetworkRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ArtistSocialNetworkPreconditionServiceImplementation implements ArtistSocialNetworkPreconditionService {

    private final ArtistSocialNetworkRepository artistSocialNetworkRepository;

    @Autowired
    public ArtistSocialNetworkPreconditionServiceImplementation(ArtistSocialNetworkRepository artistSocialNetworkRepository) {
        this.artistSocialNetworkRepository = artistSocialNetworkRepository;
    }

    @Override
    public ArtistsSocialNetworks getByIdIfExists(Long recordId) {
        return this.artistSocialNetworkRepository.findById(recordId).orElseThrow(
                () -> new NotFoundException("Record about artist's social network by id=" + recordId + " was not found")
        );
    }

    @Override
    public Set<ArtistsSocialNetworks> findByArtistId(Long artistId) {
        return this.artistSocialNetworkRepository.findAllByArtistId(artistId).stream().collect(Collectors.toSet());
    }

    @Override
    public void checkSocialNetworkNameUniqueness(Long artistId, String socialNetworkName) {
        Set<String> socialNetworks = this.findByArtistId(artistId).stream()
                .map(socialNetwork -> socialNetwork.getSocialNetwork())
                .collect(Collectors.toSet());

        if (socialNetworks.contains(socialNetworkName))
            throw new ConflictException("Artist (id=" + artistId + ") already has social network '" + socialNetworkName + "'");
    }
}
