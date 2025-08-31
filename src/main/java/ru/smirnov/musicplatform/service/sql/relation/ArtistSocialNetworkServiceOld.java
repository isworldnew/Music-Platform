package ru.smirnov.musicplatform.service.sql.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.dto.old.relation.ArtistSocialNetworkDto;
import ru.smirnov.musicplatform.entity.relation.ArtistsSocialNetworks;
import ru.smirnov.musicplatform.mapper.old.ArtistSocialNetworkMapperOld;
import ru.smirnov.musicplatform.repository.relation.ArtistSocialNetworkRepository;

import java.util.List;

@Service
public class ArtistSocialNetworkServiceOld {

    private final ArtistSocialNetworkRepository artistSocialNetworkRepository;
    private final ArtistSocialNetworkMapperOld artistSocialNetworkMapper;

    @Autowired
    public ArtistSocialNetworkServiceOld(ArtistSocialNetworkRepository artistSocialNetworkRepository, ArtistSocialNetworkMapperOld artistSocialNetworkMapper) {
        this.artistSocialNetworkRepository = artistSocialNetworkRepository;
        this.artistSocialNetworkMapper = artistSocialNetworkMapper;
    }

    public Long save(Long artistId, String socialNetworkName, String reference) {
        return this.artistSocialNetworkRepository.save(artistId, socialNetworkName, reference);
    }

    public List<ArtistsSocialNetworks> findAllByArtistId(Long artistId) {
        return this.artistSocialNetworkRepository.findAllByArtistId(artistId);
    }

    public List<ArtistSocialNetworkDto> findAllArtistSocialNetworkDtoByArtistId(Long artistId) {
        List<ArtistsSocialNetworks> rawArtistsSocialNetworks = this.artistSocialNetworkRepository.findAllByArtistId(artistId);

        return rawArtistsSocialNetworks.stream().map(
                rawEntity -> this.artistSocialNetworkMapper.artistsSocialNetworksEntityToArtistSocialNetworkDto(rawEntity)
        ).toList();
    }

}
