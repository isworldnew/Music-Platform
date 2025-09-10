package ru.smirnov.demandservice.mapper.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.smirnov.demandservice.dto.DistributorRegistrationClaimRequest;
import ru.smirnov.demandservice.dto.DistributorRegistrationClaimResponse;
import ru.smirnov.demandservice.dto.DistributorRegistrationClaimShortcutResponse;
import ru.smirnov.demandservice.entity.auxiliary.DistributorData;
import ru.smirnov.demandservice.entity.domain.DistributorRegistrationClaim;
import ru.smirnov.demandservice.mapper.abstraction.DistributorRegistrationClaimMapper;
import ru.smirnov.dtoregistry.dto.authentication.LoginRequest;
import ru.smirnov.dtoregistry.entity.auxiliary.DistributorType;
import ru.smirnov.dtoregistry.message.DistributorRegistrationMessage;

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

    @Override
    public DistributorRegistrationMessage distributorRegistrationClaimToDistributorRegistrationMessage(DistributorRegistrationClaim claim) {

        LoginRequest accountData = new LoginRequest();
        accountData.setUsername(claim.getDistributorData().getUsername());
        accountData.setPassword(claim.getDistributorData().getPassword());

        DistributorRegistrationMessage message = new DistributorRegistrationMessage();
        message.setAccountData(accountData);
        message.setName(claim.getDistributorData().getName());
        message.setDescription(claim.getDistributorData().getDescription());
        message.setDistributorType(DistributorType.valueOf(claim.getDistributorData().getDistributorType()));

        return message;
    }

    @Override
    public DistributorRegistrationClaimShortcutResponse toShortcut(DistributorRegistrationClaim claim) {
        DistributorRegistrationClaimShortcutResponse shortcut = new DistributorRegistrationClaimShortcutResponse();
        shortcut.setId(claim.getId());
        shortcut.setStatus(claim.getStatus());
        shortcut.setCreationDateTime(claim.getCreationDateTime());
        shortcut.setExpirationDateTime(claim.getExpirationDateTime());
        return shortcut;
    }

    @Override
    public DistributorRegistrationClaimResponse toResponse(DistributorRegistrationClaim claim) {
        DistributorRegistrationClaimResponse dto = new DistributorRegistrationClaimResponse();
        dto.setId(claim.getId());
        dto.setAdminId(claim.getAdminId());
        dto.setCreationDateTime(claim.getCreationDateTime());
        dto.setExpirationDateTime(claim.getExpirationDateTime());
        dto.setStatus(claim.getStatus());
        dto.setName(claim.getDistributorData().getName());
        dto.setDescription(claim.getDistributorData().getDescription());
        dto.setDistributorType(claim.getDistributorData().getDistributorType());
        dto.setUsername(claim.getDistributorData().getUsername());
        return dto;
    }
}
