package ru.smirnov.musicplatform.precondition.implementation.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.precondition.abstraction.relation.DistributorByArtistPreconditionService;
import ru.smirnov.musicplatform.repository.relation.DistributorByArtistRepository;
import ru.smirnov.musicplatform.service.abstraction.relation.DistributorByArtistService;

@Service
public class DistributorByArtistPreconditionServiceImplementation implements DistributorByArtistPreconditionService {

    private final DistributorByArtistRepository distributorByArtistRepository;

    @Autowired
    public DistributorByArtistPreconditionServiceImplementation(DistributorByArtistRepository distributorByArtistRepository) {
        this.distributorByArtistRepository = distributorByArtistRepository;
    }

    @Override
    public void checkActiveRelationBetweenDistributorAndArtistExistence(Long distributorId, Long artistId) {
        // в целом, отсюда должно возвращаться либо 1, либо 0
        long amountOfRelations = this.distributorByArtistRepository.findActiveRelationBetweenDistributorAndArtist(distributorId, artistId);

        if (amountOfRelations == 0)
            throw new ForbiddenException("Distributor (id=" + distributorId + ") has no 'ACTIVE' distribution status with artist (id=" + artistId + ")");
    }
}
