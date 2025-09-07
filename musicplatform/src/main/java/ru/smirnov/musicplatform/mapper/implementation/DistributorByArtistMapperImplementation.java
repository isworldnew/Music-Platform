package ru.smirnov.musicplatform.mapper.implementation;

import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.relation.DistributorByArtistResponse;
import ru.smirnov.musicplatform.entity.audience.Distributor;
import ru.smirnov.musicplatform.entity.auxiliary.enums.DistributionStatus;
import ru.smirnov.musicplatform.mapper.abstraction.DistributorByArtistMapper;

@Component
public class DistributorByArtistMapperImplementation implements DistributorByArtistMapper {

    @Override
    public DistributorByArtistResponse distributorEntityToDistributorByArtistResponse(Long relationId, DistributionStatus status, Distributor distributor) {
        DistributorByArtistResponse dto = new DistributorByArtistResponse();
        dto.setId(relationId);
        dto.setDistributorId(distributor.getId());
        dto.setName(distributor.getName());
        dto.setType(distributor.getDistributorType().name());
        dto.setStatus(status.name());
        return dto;
    }
}
