package ru.smirnov.demandservice.service.implementation.auxiliary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.smirnov.demandservice.service.abstraction.auxiliary.ClaimAssignService;
import ru.smirnov.demandservice.service.abstraction.domain.AdminDataService;
import ru.smirnov.demandservice.service.abstraction.domain.DistributorRegistrationClaimService;
import ru.smirnov.demandservice.service.abstraction.domain.TrackClaimService;

@Service @Primary
public class StrategyClaimAssignService implements ClaimAssignService {

    private final TrackClaimService trackClaimService;
    private final DistributorRegistrationClaimService distributorRegistrationClaimService;
    private final AdminDataService adminDataService;

    @Autowired
    public StrategyClaimAssignService(TrackClaimService trackClaimService, DistributorRegistrationClaimService distributorRegistrationClaimService, AdminDataService adminDataService) {
        this.trackClaimService = trackClaimService;
        this.distributorRegistrationClaimService = distributorRegistrationClaimService;
        this.adminDataService = adminDataService;
    }

    // ну и +- равномерно распределять между ними заявки (среди тех, у кого их меньше всего)
    // глянуть статистику по всем админам среди разных видов заявок... глянуть, сколько всего админов...
    // ну и как-то выбрать

    @Override
    public Long assignTo() {
        return 0L;
    }

}
