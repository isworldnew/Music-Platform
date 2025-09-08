package ru.smirnov.demandservice.mapper.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.smirnov.demandservice.dto.DistributorRegistrationClaimRequest;
import ru.smirnov.demandservice.entity.auxiliary.DistributorData;
import ru.smirnov.demandservice.entity.domain.DistributorRegistrationClaim;
import ru.smirnov.demandservice.mapper.abstraction.DistributorRegistrationClaimMapper;

@Component
public class DistributorRegistrationClaimMapperImplementation implements DistributorRegistrationClaimMapper {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public DistributorRegistrationClaimMapperImplementation(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public DistributorRegistrationClaim distributorRegistrationClaimRequestToDistributorRegistrationClaimEntity(DistributorRegistrationClaimRequest dto, Long adminId) {
        DistributorData distributorData = new DistributorData();
        distributorData.setName(dto.getName());
        distributorData.setDescription(dto.getDescription());
        distributorData.setUsername(dto.getUsername());
        distributorData.setPassword(this.bCryptPasswordEncoder.encode(dto.getPassword()));
        distributorData.setDistributorType(dto.getDistributorType());

        DistributorRegistrationClaim claim = new DistributorRegistrationClaim();
        claim.setDistributorData(distributorData);
        claim.setAdminId(adminId);
        return claim;
    }

}
