package ru.smirnov.musicplatform.service.implementation.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.precondition.abstraction.domain.AlbumPreconditionService;
import ru.smirnov.musicplatform.repository.relation.SavedAlbumRepository;
import ru.smirnov.musicplatform.service.abstraction.relation.SavedAlbumService;

@Service
public class SavedAlbumServiceImplementation implements SavedAlbumService {

    private final SavedAlbumRepository savedAlbumRepository;
    private final AlbumPreconditionService albumPreconditionService;

    @Autowired
    public SavedAlbumServiceImplementation(
            SavedAlbumRepository savedAlbumRepository,
            AlbumPreconditionService albumPreconditionService
    ) {
        this.savedAlbumRepository = savedAlbumRepository;
        this.albumPreconditionService = albumPreconditionService;
    }

    @Override
    @Transactional
    public Long addAlbum(Long albumId, DataForToken tokenData) {
        Album album = this.albumPreconditionService.getByIdIfExists(albumId);

        а если он станет не PUBLIC после сохранения?

        if (!album.getAccessLevel().isAvailable())
            throw new ForbiddenException("Album (id=" + albumId + ") is not PUBLIC");

        try {
            return this.savedAlbumRepository.save(tokenData.getEntityId(), albumId);
        }
        catch (DataIntegrityViolationException e) {
            throw new ConflictException("Album (id=" + albumId + ") already saved by user (id=" + tokenData.getEntityId() + ")");
        }

    }

    @Override
    @Transactional
    public void removeAlbum(Long albumId, DataForToken tokenData) {
        this.savedAlbumRepository.delete(tokenData.getEntityId(), albumId);
    }
}
