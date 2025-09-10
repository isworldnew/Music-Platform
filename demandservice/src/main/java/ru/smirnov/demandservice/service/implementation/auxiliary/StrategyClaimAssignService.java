package ru.smirnov.demandservice.service.implementation.auxiliary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.smirnov.demandservice.repository.AdminDataRepository;
import ru.smirnov.demandservice.repository.DistributorRegistrationClaimRepository;
import ru.smirnov.demandservice.repository.TrackClaimRepository;
import ru.smirnov.demandservice.service.abstraction.auxiliary.ClaimAssignService;
import ru.smirnov.demandservice.service.abstraction.domain.AdminDataService;
import ru.smirnov.demandservice.service.abstraction.domain.DistributorRegistrationClaimService;
import ru.smirnov.demandservice.service.abstraction.domain.TrackClaimService;

@Service @Primary
public class StrategyClaimAssignService implements ClaimAssignService {

    private final TrackClaimRepository trackClaimRepository;
    private final DistributorRegistrationClaimRepository distributorRegistrationClaimRepository;
    private final AdminDataRepository adminDataRepository;

    @Autowired
    public StrategyClaimAssignService(
            TrackClaimRepository trackClaimRepository,
            DistributorRegistrationClaimRepository distributorRegistrationClaimRepository,
            AdminDataRepository adminDataRepository
    ) {
        this.trackClaimRepository = trackClaimRepository;
        this.distributorRegistrationClaimRepository = distributorRegistrationClaimRepository;
        this.adminDataRepository = adminDataRepository;
    }

    // ну и +- равномерно распределять между ними заявки (среди тех, у кого их меньше всего)
    // глянуть статистику по всем админам среди разных видов заявок... глянуть, сколько всего админов...
    // ну и как-то выбрать

    не забудь реализовать распределение

    @Override
    public Long assignTo() {
        return 0L;
    }

}
