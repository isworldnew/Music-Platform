package ru.smirnov.musicplatform.mapper;

import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.relation.ArtistSocialNetworkDto;
import ru.smirnov.musicplatform.entity.relation.ArtistsSocialNetworks;

@Component
public class ArtistSocialNetworkMapper {

    public ArtistSocialNetworkDto artistsSocialNetworksEntityToArtistSocialNetworkDto(ArtistsSocialNetworks entity) {
        ArtistSocialNetworkDto dto = new ArtistSocialNetworkDto();
        dto.setId(entity.getId());
        dto.setName(entity.getSocialNetwork());
        dto.setReference(entity.getReference());
        return dto;
    }

}
