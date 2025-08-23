package ru.smirnov.musicplatform.service.sql.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.entity.auxiliary.enums.DistributionStatus;
import ru.smirnov.musicplatform.repository.relation.DistributorByArtistRepository;

@Service
public class DistributorByArtistService {

    private final DistributorByArtistRepository distributorByArtistRepository;

    @Autowired
    public DistributorByArtistService(DistributorByArtistRepository distributorByArtistRepository) {
        this.distributorByArtistRepository = distributorByArtistRepository;
    }

    public Long save(Long distributorId, Long artistId, DistributionStatus status) {
        return this.distributorByArtistRepository.save(distributorId, artistId, status.name());
    }

}
