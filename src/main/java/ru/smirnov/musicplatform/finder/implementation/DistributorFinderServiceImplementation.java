package ru.smirnov.musicplatform.finder.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.dto.audience.distributor.DistributorShortcutResponse;
import ru.smirnov.musicplatform.entity.audience.Distributor;
import ru.smirnov.musicplatform.finder.abstraction.DistributorFinderService;
import ru.smirnov.musicplatform.mapper.abstraction.DistributorMapper;
import ru.smirnov.musicplatform.repository.audience.finder.abstraction.DistributorFinderRepository;

import java.util.List;

@Repository
public class DistributorFinderServiceImplementation implements DistributorFinderService {

    private final DistributorFinderRepository distributorFinderRepository;
    private final DistributorMapper distributorMapper;

    @Autowired
    public DistributorFinderServiceImplementation(DistributorFinderRepository distributorFinderRepository, DistributorMapper distributorMapper) {
        this.distributorFinderRepository = distributorFinderRepository;
        this.distributorMapper = distributorMapper;
    }

    @Override
    public List<DistributorShortcutResponse> searchDistributors(String searchRequest) {
        List<Distributor> distributors = this.distributorFinderRepository.searchDistributors(searchRequest);

        return distributors.stream()
                .map(distributor -> this.distributorMapper.distributorEntityToDistributorShortcutResponse(distributor))
                .toList();
    }
}
