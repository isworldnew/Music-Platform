package ru.smirnov.demandservice.service.implementation.auxiliary;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.smirnov.demandservice.service.abstraction.auxiliary.ClaimAssignService;
import ru.smirnov.demandservice.service.abstraction.domain.DistributorRegistrationClaimService;
import ru.smirnov.demandservice.service.abstraction.domain.TrackClaimService;

@Service @Primary
public class StrategyClaimAssignService implements ClaimAssignService {

    // вот сюда заинжектить сервис, ответственный за местную информацию об админах
    // сервис о заявках...
    private final TrackClaimService trackClaimService;
    private final DistributorRegistrationClaimService distributorRegistrationClaimService;
    private final AdminData

    // ну и +- равномерно распределять между ними заявки (среди тех, у кого их меньше всего)

    @Override
    public Long assignTo() {
        return 0L;
    }
}
