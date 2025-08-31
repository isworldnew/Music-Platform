package ru.smirnov.musicplatform.service.implementation.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionAccessLevelRequest;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionRequest;
import ru.smirnov.musicplatform.entity.auxiliary.enums.MusicCollectionAccessLevel;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.mapper.abstraction.AlbumMapper;
import ru.smirnov.musicplatform.precondition.abstraction.domain.AlbumPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.domain.ArtistPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.relation.DistributorByArtistPreconditionService;
import ru.smirnov.musicplatform.repository.domain.AlbumRepository;
import ru.smirnov.musicplatform.service.abstraction.domain.AlbumService;

@Service
public class AlbumServiceImplementation implements AlbumService {

    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;
    private final AlbumPreconditionService albumPreconditionService;

    private final ArtistPreconditionService artistPreconditionService;
    private final DistributorByArtistPreconditionService distributorByArtistPreconditionService;

    @Autowired
    public AlbumServiceImplementation(
            AlbumRepository albumRepository,
            AlbumMapper albumMapper,
            AlbumPreconditionService albumPreconditionService,
            ArtistPreconditionService artistPreconditionService,
            DistributorByArtistPreconditionService distributorByArtistPreconditionService
    ) {
        this.albumRepository = albumRepository;
        this.albumMapper = albumMapper;
        this.albumPreconditionService = albumPreconditionService;
        this.artistPreconditionService = artistPreconditionService;
        this.distributorByArtistPreconditionService = distributorByArtistPreconditionService;
    }

    @Override
    @Transactional
    public Long createAlbum(Long artistId, MusicCollectionRequest dto, DataForToken tokenData) {
        /*
        Проверяем, что:
        [v] Данный исполнитель вообще существует
        [v] Дистрибьютор, сделавший запрос, имеет ACTIVE-связь с данным исполнителем
        [v] У данного исполнителя ещё нет альбомов с таким названием
        */
        Artist artist = this.artistPreconditionService.getByIdIfExists(artistId);
        this.distributorByArtistPreconditionService.checkActiveRelationBetweenDistributorAndArtistExistence(tokenData.getEntityId(), artistId);
        this.albumPreconditionService.existsByNameAndArtistId(dto.getName(), artistId);

        Album album = this.albumMapper.musicCollectionRequestToAlbumEntity(dto);
        this.albumRepository.save(album);

        return album.getId();
    }

    @Override
    @Transactional
    public void updateAlbum(Long albumId, MusicCollectionRequest dto, DataForToken tokenData) {
        /*
        Проверяем, что:
        [v] Данный альбом вообще существует
        [v] Переданное имя либо не изменилось, либо уникально среди альбомов исполнителя
        [v] Дистрибьютор, сделавший запрос, имеет ACTIVE-связь с данным исполнителем
        */
        Album album = this.albumPreconditionService.getByIdIfExistsAndNameIsUnique(albumId, dto.getName());
        this.distributorByArtistPreconditionService.checkActiveRelationBetweenDistributorAndArtistExistence(tokenData.getEntityId(), album.getArtist().getId());

        нет проверки на то, что плейлист принадлежит исполнителю

        album.setName(dto.getName());
        album.setDescription(dto.getDescription());

        this.albumRepository.save(album);
    }

    @Override
    @Transactional
    public void updateAlbumAccessLevel(Long albumId, MusicCollectionAccessLevelRequest dto, DataForToken tokenData) {
        /*
        Проверяем, что:
        [v] Данный альбом вообще существует
        [v] Дистрибьютор, сделавший запрос, имеет ACTIVE-связь с исполнителем данного альбома
        */
        Album album = this.albumPreconditionService.getByIdIfExists(albumId);
        this.distributorByArtistPreconditionService.checkActiveRelationBetweenDistributorAndArtistExistence(tokenData.getEntityId(), album.getArtist().getId());

        нет проверки на то, что плейлист принадлежит исполнителю

        album.setAccessLevel(MusicCollectionAccessLevel.valueOf(dto.getAccessLevel()));

        this.albumRepository.save(album);

        не нужно ли удалить плейлист из сохранённых, если он был PUBLIC, а стал PRIVATE?
    }


}
