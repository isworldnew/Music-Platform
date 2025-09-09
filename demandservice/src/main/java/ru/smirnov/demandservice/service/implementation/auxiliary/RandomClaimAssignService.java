package ru.smirnov.demandservice.service.implementation.auxiliary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.demandservice.client.abstraction.AdminClient;
import ru.smirnov.demandservice.service.abstraction.auxiliary.ClaimAssignService;
import ru.smirnov.demandservice.util.Randomizer;

import java.util.List;

@Service
public class RandomClaimAssignService implements ClaimAssignService {

    private final AdminClient adminClient;

    @Autowired
    public RandomClaimAssignService(AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    @Override
    public Long assignTo() {
        List<Long> admins = this.adminClient.getAllEnabledAdmins();
        return admins.get(Randomizer.getRandomIndex(admins));
    }

}
