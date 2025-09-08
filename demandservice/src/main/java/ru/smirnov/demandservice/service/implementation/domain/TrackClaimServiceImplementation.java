package ru.smirnov.demandservice.service.implementation.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.demandservice.client.abstraction.AdminClient;
import ru.smirnov.demandservice.client.abstraction.TrackClient;
import ru.smirnov.demandservice.entity.domain.TrackClaim;
import ru.smirnov.demandservice.mapper.abstraction.TrackClaimMapper;
import ru.smirnov.demandservice.repository.TrackClaimRepository;
import ru.smirnov.demandservice.service.abstraction.domain.TrackClaimService;
import ru.smirnov.demandservice.util.Randomizer;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;

import java.util.List;

@Service
public class TrackClaimServiceImplementation implements TrackClaimService {

    private final TrackClaimRepository trackClaimRepository;
    private final TrackClaimMapper trackClaimMapper;
    private final TrackClient trackClient;
    private final AdminClient adminClient;

    @Autowired
    public TrackClaimServiceImplementation(
            TrackClaimRepository trackClaimRepository,
            TrackClaimMapper trackClaimMapper,
            TrackClient trackClient,
            AdminClient adminClient
    ) {
        this.trackClaimRepository = trackClaimRepository;
        this.trackClaimMapper = trackClaimMapper;
        this.trackClient = trackClient;
        this.adminClient = adminClient;
    }

    @Override
    public Long addTrackClaim(Long trackId, DataForToken tokenData) {
        /*
        Проверить, что:
        [v] трек с данным id существует
        [v] извелчь id администраторов с enabled аккаунтами
        */
        this.trackClient.trackExistsById(trackId);
        List<Long> admins = this.adminClient.getAllEnabledAdmins();

        Long adminId = admins.get(Randomizer.getRandomIndex(admins));

        TrackClaim trackClaim = this.trackClaimMapper.initTrackClaim(trackId, adminId, tokenData.getEntityId());

        System.out.println(trackClaim);

        return this.trackClaimRepository.save(trackClaim).getId();
    }
}
