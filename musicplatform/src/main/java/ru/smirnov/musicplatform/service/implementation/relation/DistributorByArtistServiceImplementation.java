package ru.smirnov.musicplatform.service.implementation.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.relation.DistributorByArtistRelationRequest;
import ru.smirnov.musicplatform.entity.auxiliary.enums.DistributionStatus;
import ru.smirnov.musicplatform.precondition.abstraction.relation.DistributorByArtistPreconditionService;
import ru.smirnov.musicplatform.repository.relation.DistributorByArtistRepository;
import ru.smirnov.musicplatform.service.abstraction.relation.DistributorByArtistService;

@Service
public class DistributorByArtistServiceImplementation implements DistributorByArtistService  {

    private final DistributorByArtistRepository distributorByArtistRepository;
    private final DistributorByArtistPreconditionService distributorByArtistPreconditionService;

    @Autowired
    public DistributorByArtistServiceImplementation(
            DistributorByArtistRepository distributorByArtistRepository,
            DistributorByArtistPreconditionService distributorByArtistPreconditionService
    ) {
        this.distributorByArtistRepository = distributorByArtistRepository;
        this.distributorByArtistPreconditionService = distributorByArtistPreconditionService;
    }

    @Override
    @Transactional
    public Long save(Long distributorId, Long artistId, DistributionStatus status) {

        // вот тут дописать побольше проверок

        return this.distributorByArtistRepository.save(distributorId, artistId, status.name());
    }

    @Override
    @Transactional
    public void updateRelationBetweenDistributorAndArtist(Long distributorId, Long artistId, DistributorByArtistRelationRequest dto, DataForToken tokenData) {

    }

}
