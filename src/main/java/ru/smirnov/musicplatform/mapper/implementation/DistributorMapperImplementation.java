package ru.smirnov.musicplatform.mapper.implementation;

import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.audience.distributor.DistributorResponse;
import ru.smirnov.musicplatform.entity.audience.Distributor;
import ru.smirnov.musicplatform.mapper.abstraction.DistributorMapper;

@Component
public class DistributorMapperImplementation implements DistributorMapper {

    @Override
    public DistributorResponse distributorEntityToDistributorResponse(Distributor distributor) {
        DistributorResponse dto = new DistributorResponse();
        dto.setDistributorId(distributor.getId());
        dto.setAccountId(distributor.getAccount().getId());
        dto.setUsername(distributor.getAccount().getUsername());
        dto.setName(distributor.getName());
        dto.setDescription(distributor.getDescription());
        dto.setDistributorType(distributor.getDistributorType().name());
        dto.setRegistrationDate(distributor.getRegistrationDate());
        return dto;
    }
}
