package ru.smirnov.musicplatform.service.tmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.tmp.ArtistRequest;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.repository.domain.ArtistRepository;
import ru.smirnov.musicplatform.service.interfaces.domain.ArtistService;
import ru.smirnov.musicplatform.temporary.ArtistMapper;
import ru.smirnov.musicplatform.precondition.abstraction.domain.ArtistPreconditionService;
import ru.smirnov.musicplatform.temporary.DistributorByArtistService;

@Service
public class ArtistServiceImplementation implements ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;

    private final ArtistPreconditionService artistPreconditionService;
    private final DistributorByArtistService distributorByArtistService;

    @Autowired
    public ArtistServiceImplementation(
            ArtistRepository artistRepository,
            ArtistMapper artistMapper,
            ArtistPreconditionService artistPreconditionService,
            DistributorByArtistService distributorByArtistService
    ) {
        this.artistRepository = artistRepository;
        this.artistMapper = artistMapper;
        this.artistPreconditionService = artistPreconditionService;
        this.distributorByArtistService = distributorByArtistService;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Long createArtist(ArtistRequest dto, DataForToken tokenData) {
        Long distributorId = tokenData.getEntityId();

        this.artistPreconditionService.existsByName(dto.getName());

        Artist artist = this.artistMapper.artistRequestToArtistEntity(dto);



    }


}
