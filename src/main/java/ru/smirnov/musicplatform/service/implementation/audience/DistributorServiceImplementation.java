package ru.smirnov.musicplatform.service.implementation.audience;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.audience.distributor.DistributorRequest;
import ru.smirnov.musicplatform.dto.audience.distributor.DistributorResponse;
import ru.smirnov.musicplatform.mapper.abstraction.DistributorMapper;
import ru.smirnov.musicplatform.precondition.abstraction.audience.DistributorPreconditionService;
import ru.smirnov.musicplatform.repository.audience.DistributorRepository;
import ru.smirnov.musicplatform.service.abstraction.audience.DistributorService;

@Service
public class DistributorServiceImplementation implements DistributorService {

    private final DistributorRepository distributorRepository;
    private final DistributorPreconditionService distributorPreconditionService;
    private final DistributorMapper distributorMapper;

    @Autowired
    public DistributorServiceImplementation(
            DistributorRepository distributorRepository,
            DistributorPreconditionService distributorPreconditionService,
            DistributorMapper distributorMapper
    ) {
        this.distributorRepository = distributorRepository;
        this.distributorPreconditionService = distributorPreconditionService;
        this.distributorMapper = distributorMapper;
    }

    @Override
    public DistributorResponse getDistributorData(DataForToken tokenData) {
        return null;
    }

    @Override
    public void updateDistributorData(DistributorRequest dto, DataForToken tokenData) {

    }
}
