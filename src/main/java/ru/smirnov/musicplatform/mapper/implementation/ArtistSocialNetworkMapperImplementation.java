package ru.smirnov.musicplatform.mapper.implementation;

import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.tmp.ArtistSocialNetworkResponse;
import ru.smirnov.musicplatform.entity.relation.ArtistsSocialNetworks;
import ru.smirnov.musicplatform.mapper.abstraction.ArtistSocialNetworkMapper;

@Component
public class ArtistSocialNetworkMapperImplementation implements ArtistSocialNetworkMapper {

    @Override
    public ArtistSocialNetworkResponse artistSocialNetworkEntityToArtistSocialNetworkResponse(ArtistsSocialNetworks entity) {
        ArtistSocialNetworkResponse dto = new ArtistSocialNetworkResponse();
        dto.setId(entity.getId());
        dto.setName(entity.getSocialNetwork());
        dto.setReference(entity.getReference());
        return dto;
    }

}
