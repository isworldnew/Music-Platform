package ru.smirnov.musicplatform.precondition.implementation.audience;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.entity.audience.Distributor;
import ru.smirnov.musicplatform.precondition.abstraction.audience.DistributorPreconditionService;
import ru.smirnov.musicplatform.repository.audience.DistributorRepository;
import ru.smirnov.musicplatform.service.abstraction.audience.DistributorService;

@Service
public class DistributorPreconditionServiceImplementation implements DistributorPreconditionService {

    private final DistributorRepository distributorRepository;

    @Autowired
    public DistributorPreconditionServiceImplementation(DistributorRepository distributorRepository) {
        this.distributorRepository = distributorRepository;
    }

    @Override
    public Distributor findByIdIfExists(Long distributorId) {
        return null;
    }
}
