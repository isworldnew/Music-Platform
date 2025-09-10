package ru.smirnov.demandservice.service.implementation.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.demandservice.client.abstraction.TrackClient;
import ru.smirnov.demandservice.entity.domain.TrackClaim;
import ru.smirnov.demandservice.kafka.producer.abstraction.KafkaTrackProducer;
import ru.smirnov.demandservice.mapper.abstraction.TrackClaimMapper;
import ru.smirnov.demandservice.repository.TrackClaimRepository;
import ru.smirnov.demandservice.service.abstraction.auxiliary.ClaimAssignService;
import ru.smirnov.demandservice.service.abstraction.domain.TrackClaimService;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.dtoregistry.dto.domain.TrackAccessLevelRequest;
import ru.smirnov.dtoregistry.dto.domain.TrackClaimRequest;
import ru.smirnov.dtoregistry.entity.auxiliary.DemandStatus;
import ru.smirnov.dtoregistry.entity.auxiliary.TrackStatus;
import ru.smirnov.dtoregistry.message.TrackStatusMessage;

@Service
public class TrackClaimServiceImplementation implements TrackClaimService {

    private final TrackClaimRepository trackClaimRepository;
    private final TrackClaimMapper trackClaimMapper;
    private final TrackClient trackClient;
    private final ClaimAssignService claimAssignService;
    private final KafkaTrackProducer kafkaTrackProducer;

    @Autowired
    public TrackClaimServiceImplementation(
            TrackClaimRepository trackClaimRepository,
            TrackClaimMapper trackClaimMapper,
            TrackClient trackClient,
            ClaimAssignService claimAssignService, KafkaTrackProducer kafkaTrackProducer
    ) {
        this.trackClaimRepository = trackClaimRepository;
        this.trackClaimMapper = trackClaimMapper;
        this.trackClient = trackClient;
        this.claimAssignService = claimAssignService;
        this.kafkaTrackProducer = kafkaTrackProducer;
    }

    @Override
    public Long addTrackClaim(Long trackId, DataForToken tokenData) {
        /*
        Проверить, что:
        [v] трек с данным id существует
        [v] извелчь id администраторов с enabled аккаунтами
        */
        this.trackClient.trackExistsById(trackId);

        Long adminId = this.claimAssignService.assignTo();

        TrackClaim trackClaim = this.trackClaimMapper.initTrackClaim(trackId, adminId, tokenData.getEntityId());

        System.out.println(trackClaim);

        return this.trackClaimRepository.save(trackClaim).getId();
    }

    @Override
    @Transactional
    public void processTrackClaim(Long claimId, TrackClaimRequest dto) {
        TrackClaim trackClaim = this.trackClaimRepository.findById(claimId).get();
        trackClaim.setStatus(DemandStatus.valueOf(dto.getDemandStatus()));
        this.kafkaTrackProducer.sendMessage(new TrackStatusMessage(
                trackClaim.getTrackId(),
                TrackStatus.valueOf(dto.getAccessLevel())
        ));
    }

}
