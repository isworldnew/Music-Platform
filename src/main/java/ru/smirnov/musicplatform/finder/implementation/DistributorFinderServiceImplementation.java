package ru.smirnov.musicplatform.finder.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.dto.audience.distributor.DistributorShortcutResponse;
import ru.smirnov.musicplatform.dto.audience.distributor.ExtendedDistributorResponse;
import ru.smirnov.musicplatform.entity.audience.Distributor;
import ru.smirnov.musicplatform.finder.abstraction.DistributorFinderService;
import ru.smirnov.musicplatform.mapper.abstraction.DistributorMapper;
import ru.smirnov.musicplatform.precondition.abstraction.audience.DistributorPreconditionService;
import ru.smirnov.musicplatform.repository.audience.DistributorRepository;
import ru.smirnov.musicplatform.repository.audience.finder.abstraction.DistributorFinderRepository;

import java.util.List;

@Repository
public class DistributorFinderServiceImplementation implements DistributorFinderService {

    private final DistributorPreconditionService distributorPreconditionService;
    private final DistributorFinderRepository distributorFinderRepository;
    private final DistributorRepository distributorRepository;
    private final DistributorMapper distributorMapper;

    @Autowired
    public DistributorFinderServiceImplementation(
            DistributorPreconditionService distributorPreconditionService,
            DistributorFinderRepository distributorFinderRepository,
            DistributorRepository distributorRepository,
            DistributorMapper distributorMapper
    ) {
        this.distributorPreconditionService = distributorPreconditionService;
        this.distributorFinderRepository = distributorFinderRepository;
        this.distributorRepository = distributorRepository;
        this.distributorMapper = distributorMapper;
    }

    @Override
    public List<DistributorShortcutResponse> searchDistributors(String searchRequest) {
        List<Distributor> distributors = this.distributorFinderRepository.searchDistributors(searchRequest);

        return distributors.stream()
                .map(distributor -> this.distributorMapper.distributorEntityToDistributorShortcutResponse(distributor))
                .toList();
    }

    @Override
    public ExtendedDistributorResponse getDistributorById(Long distributorId) {
        Distributor distributor = this.distributorPreconditionService.getByIdIfExists(distributorId);
        return this.distributorMapper.distributorEntityToExtendedDistributorResponse(distributor);
    }
}
