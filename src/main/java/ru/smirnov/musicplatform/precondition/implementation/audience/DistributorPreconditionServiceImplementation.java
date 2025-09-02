package ru.smirnov.musicplatform.precondition.implementation.audience;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.precondition.abstraction.audience.DistributorPreconditionService;
import ru.smirnov.musicplatform.service.abstraction.audience.DistributorService;

@Service
public class DistributorPreconditionServiceImplementation implements DistributorPreconditionService {

    private final DistributorService distributorService;

    @Autowired
    public DistributorPreconditionServiceImplementation(DistributorService distributorService) {
        this.distributorService = distributorService;
    }
}
