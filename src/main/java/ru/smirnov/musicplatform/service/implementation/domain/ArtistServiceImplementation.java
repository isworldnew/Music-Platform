package ru.smirnov.musicplatform.service.implementation.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistRequest;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistResponse;
import ru.smirnov.musicplatform.dto.domain.artist.ExtendedArtistResponse;
import ru.smirnov.musicplatform.entity.auxiliary.enums.DistributionStatus;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.precondition.abstraction.relation.DistributorByArtistPreconditionService;
import ru.smirnov.musicplatform.repository.domain.ArtistRepository;
import ru.smirnov.musicplatform.service.abstraction.domain.ArtistService;
import ru.smirnov.musicplatform.mapper.abstraction.ArtistMapper;
import ru.smirnov.musicplatform.precondition.abstraction.domain.ArtistPreconditionService;
import ru.smirnov.musicplatform.service.abstraction.relation.DistributorByArtistService;

@Service
public class ArtistServiceImplementation implements ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;
    private final ArtistPreconditionService artistPreconditionService;

    private final DistributorByArtistService distributorByArtistService;
    private final DistributorByArtistPreconditionService distributorByArtistPreconditionService;

    @Autowired
    public ArtistServiceImplementation(
            ArtistRepository artistRepository,
            ArtistMapper artistMapper,
            ArtistPreconditionService artistPreconditionService,
            DistributorByArtistService distributorByArtistService,
            DistributorByArtistPreconditionService distributorByArtistPreconditionService
    ) {
        this.artistRepository = artistRepository;
        this.artistMapper = artistMapper;
        this.artistPreconditionService = artistPreconditionService;
        this.distributorByArtistService = distributorByArtistService;
        this.distributorByArtistPreconditionService = distributorByArtistPreconditionService;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Long createArtist(ArtistRequest dto, DataForToken tokenData) {
        Long distributorId = tokenData.getEntityId();

        this.artistPreconditionService.existsByName(dto.getName());

        Artist artist = this.artistMapper.artistRequestToArtistEntity(dto);
        this.artistRepository.save(artist);

        this.distributorByArtistService.save(distributorId, artist.getId(), DistributionStatus.ACTIVE);

        return artist.getId();
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void updateArtist(Long artistId, ArtistRequest dto, DataForToken tokenData) {
        /*
        Сначала имеет смысл проверить, что:
        [v] Исполнитель с таким id вообще существует
        [v] Имя из DTO либо принадлежит ему, либо уникально среди исполнителей
        */
        Artist artist = this.artistPreconditionService.getByIdIfExistsAndNameIsUnique(artistId, dto.getName());

        /*
        Только потом имеет смысл проверить:
        [v] может ли данный дистрибьютор взаимодействовать с данным исполнителем
        (чтобы избежать дублируюущихся запросов на существование исполнителя по id)
        */
        this.distributorByArtistPreconditionService.checkActiveRelationBetweenDistributorAndArtistExistence(tokenData.getEntityId(), artistId);

        artist.setName(dto.getName());
        artist.setDescription(dto.getDescription());
    }

    @Override
    public ArtistResponse getArtistDataById(Long artistId) {
        Artist artist = this.artistPreconditionService.getByIdIfExists(artistId);
        return this.artistMapper.artistEntityToArtistResponse(artist);
    }

    @Override
    public ExtendedArtistResponse getExtendedArtistDataById(Long artistId) {
        Artist artist = this.artistPreconditionService.getByIdIfExists(artistId);
        return this.artistMapper.artistEntityToExtendedArtistResponse(artist);
    }

    // а получение всех своих исполнителей (с учётом статуса взаимодействия с ними)?
    // а поиск по ним?
    // тут тоже шорткаты понадобятся

}
