package ru.smirnov.musicplatform.service.sql.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.repository.relation.ArtistSocialNetworkRepository;

@Service
public class ArtistSocialNetworkService {

    private final ArtistSocialNetworkRepository artistSocialNetworkRepository;

    @Autowired
    public ArtistSocialNetworkService(ArtistSocialNetworkRepository artistSocialNetworkRepository) {
        this.artistSocialNetworkRepository = artistSocialNetworkRepository;
    }

    public Long save(Long artistId, String socialNetworkName, String reference) {
        return this.artistSocialNetworkRepository.save(artistId, socialNetworkName, reference);
    }
}
