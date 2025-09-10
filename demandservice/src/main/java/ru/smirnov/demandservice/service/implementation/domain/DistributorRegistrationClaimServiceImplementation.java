package ru.smirnov.demandservice.service.implementation.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.demandservice.dto.DistributorRegistrationClaimRequest;
import ru.smirnov.demandservice.entity.domain.DistributorRegistrationClaim;
import ru.smirnov.demandservice.kafka.producer.abstraction.KafkaDistributorProducer;
import ru.smirnov.demandservice.mapper.abstraction.DistributorRegistrationClaimMapper;
import ru.smirnov.demandservice.repository.DistributorRegistrationClaimRepository;
import ru.smirnov.demandservice.service.abstraction.auxiliary.ClaimAssignService;
import ru.smirnov.demandservice.service.abstraction.domain.DistributorRegistrationClaimService;
import ru.smirnov.dtoregistry.dto.authentication.LoginRequest;
import ru.smirnov.dtoregistry.dto.domain.DemandStatusRequest;
import ru.smirnov.dtoregistry.entity.auxiliary.DemandStatus;
import ru.smirnov.dtoregistry.message.DistributorRegistrationMessage;

@Service
public class DistributorRegistrationClaimServiceImplementation implements DistributorRegistrationClaimService {

    private final DistributorRegistrationClaimRepository distributorRegistrationClaimRepository;
    private final DistributorRegistrationClaimMapper distributorRegistrationClaimMapper;
    private final ClaimAssignService claimAssignService;
    private final KafkaDistributorProducer kafkaDistributorProducer;

    @Autowired
    public DistributorRegistrationClaimServiceImplementation(
            DistributorRegistrationClaimRepository distributorRegistrationClaimRepository,
            DistributorRegistrationClaimMapper distributorRegistrationClaimMapper,
            ClaimAssignService claimAssignService,
            KafkaDistributorProducer kafkaDistributorProducer
    ) {
        this.distributorRegistrationClaimRepository = distributorRegistrationClaimRepository;
        this.distributorRegistrationClaimMapper = distributorRegistrationClaimMapper;
        this.claimAssignService = claimAssignService;
        this.kafkaDistributorProducer = kafkaDistributorProducer;
    }

    @Override
    public Long addDistributorRegistrationClaim(DistributorRegistrationClaimRequest dto) {
        Long adminId = this.claimAssignService.assignTo();

        DistributorRegistrationClaim claim = this.distributorRegistrationClaimMapper.distributorRegistrationClaimRequestToDistributorRegistrationClaimEntity(
                dto, adminId
        );

        return this.distributorRegistrationClaimRepository.save(claim).getId();
    }

    @Override
    @Transactional
    public void processDistributorClaim(Long claimId, DemandStatusRequest dto) {
        DistributorRegistrationClaim claim = this.distributorRegistrationClaimRepository.findById(claimId).get();

        claim.setStatus(DemandStatus.valueOf(dto.getDemandStatus()));
        this.distributorRegistrationClaimRepository.save(claim);

        if (DemandStatus.valueOf(dto.getDemandStatus()).isAbleToRegisterDistributor()) {
            this.kafkaDistributorProducer.sendMessage(
                    this.distributorRegistrationClaimMapper.distributorRegistrationClaimToDistributorRegistrationMessage(claim)
            );
        }
    }
}
