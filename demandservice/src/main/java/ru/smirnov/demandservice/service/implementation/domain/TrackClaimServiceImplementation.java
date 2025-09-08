package ru.smirnov.demandservice.service.implementation.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.demandservice.repository.TrackClaimRepository;
import ru.smirnov.demandservice.service.abstraction.domain.TrackClaimService;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;

@Service
public class TrackClaimServiceImplementation implements TrackClaimService {

    private final TrackClaimRepository trackClaimRepository;

    @Autowired
    public TrackClaimServiceImplementation(TrackClaimRepository trackClaimRepository) {
        this.trackClaimRepository = trackClaimRepository;
    }

    @Override
    public Long addTrackClaim(Long trackId, DataForToken tokenData) {
        /*
        Проверить, что:
        [] трек с данным id существует
        [] извелчь id администраторов с enabled аккаунтами
        */
    }
}
