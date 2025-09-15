package ru.smirnov.demandservice.service.implementation.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.demandservice.dto.DistributorRegistrationClaimRequest;
import ru.smirnov.demandservice.dto.DistributorRegistrationClaimResponse;
import ru.smirnov.demandservice.dto.DistributorRegistrationClaimShortcutResponse;
import ru.smirnov.demandservice.entity.domain.DistributorRegistrationClaim;
import ru.smirnov.demandservice.kafka.producer.abstraction.KafkaDistributorProducer;
import ru.smirnov.demandservice.mapper.abstraction.DistributorRegistrationClaimMapper;
import ru.smirnov.demandservice.precondition.abstraction.DistributorClaimPreconditionService;
import ru.smirnov.demandservice.repository.DistributorRegistrationClaimRepository;
import ru.smirnov.demandservice.service.abstraction.auxiliary.ClaimAssignService;
import ru.smirnov.demandservice.service.abstraction.domain.DistributorRegistrationClaimService;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.dtoregistry.dto.domain.DemandStatusRequest;
import ru.smirnov.dtoregistry.entity.auxiliary.DemandStatus;

import java.util.Arrays;
import java.util.List;

@Service
public class DistributorRegistrationClaimServiceImplementation implements DistributorRegistrationClaimService {

    private final DistributorRegistrationClaimRepository distributorRegistrationClaimRepository;
    private final DistributorClaimPreconditionService distributorClaimPreconditionService;
    private final DistributorRegistrationClaimMapper distributorRegistrationClaimMapper;
    private final ClaimAssignService claimAssignService;
    private final KafkaDistributorProducer kafkaDistributorProducer;

    @Autowired
    public DistributorRegistrationClaimServiceImplementation(
            DistributorRegistrationClaimRepository distributorRegistrationClaimRepository,
            DistributorClaimPreconditionService distributorClaimPreconditionService,
            DistributorRegistrationClaimMapper distributorRegistrationClaimMapper,
            ClaimAssignService claimAssignService,
            KafkaDistributorProducer kafkaDistributorProducer
    ) {
        this.distributorRegistrationClaimRepository = distributorRegistrationClaimRepository;
        this.distributorClaimPreconditionService = distributorClaimPreconditionService;
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
    public void processDistributorClaim(Long claimId, DemandStatusRequest dto, DataForToken tokenData) {
        DistributorRegistrationClaim claim = this.distributorClaimPreconditionService.processClaim(claimId, tokenData.getEntityId(), dto);

        claim.setStatus(DemandStatus.valueOf(dto.getDemandStatus()));
        this.distributorRegistrationClaimRepository.save(claim);

        if (DemandStatus.valueOf(dto.getDemandStatus()).isAbleToRegisterDistributor()) {
            this.kafkaDistributorProducer.sendMessage(
                    this.distributorRegistrationClaimMapper.distributorRegistrationClaimToDistributorRegistrationMessage(claim)
            );
        }
    }

    @Override
    public List<DistributorRegistrationClaimShortcutResponse> getDistributorRegistrationClaims(Boolean relevantOnly, DataForToken tokenData) {
        List<DistributorRegistrationClaim> claims;

        List<DemandStatus> statuses = Arrays.stream(DemandStatus.values()).toList();

        if (relevantOnly) claims = this.distributorRegistrationClaimRepository.findAllByAdminIdAndRelevance(
                tokenData.getEntityId(),
                statuses.stream()
                        .filter(status -> !status.isModifying())
                        .map(status -> status.name())
                        .toList()
        );

        else claims = this.distributorRegistrationClaimRepository.findAllByAdminIdAndRelevance(
                tokenData.getEntityId(),
                statuses.stream()
                        .filter(status -> status.isModifying())
                        .map(status -> status.name())
                        .toList()
        );

        return claims.stream()
                .map(claim -> this.distributorRegistrationClaimMapper.toShortcut(claim))
                .toList();
    }

    @Override
    @Transactional
    public DistributorRegistrationClaimResponse getDistributorRegistrationClaimById(Long claimId, DataForToken tokenData) {
        DistributorRegistrationClaim claim = this.distributorClaimPreconditionService.getByidIfExistsAndBelongsToAdmin(claimId, tokenData.getEntityId());

        if (claim.getStatus().equals(DemandStatus.RECEIVED)) {
            claim.setStatus(DemandStatus.IN_PROGRESS);
            this.distributorRegistrationClaimRepository.save(claim);
        }

        return this.distributorRegistrationClaimMapper.toResponse(claim);
    }

}
