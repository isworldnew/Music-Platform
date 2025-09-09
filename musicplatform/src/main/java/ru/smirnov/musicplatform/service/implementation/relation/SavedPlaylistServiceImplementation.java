package ru.smirnov.musicplatform.service.implementation.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.entity.domain.Playlist;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.precondition.abstraction.domain.PlaylistPreconditionService;
import ru.smirnov.musicplatform.repository.relation.SavedPlaylistRepository;
import ru.smirnov.musicplatform.service.abstraction.relation.SavedPlaylistService;

// [v] checked
@Service
public class SavedPlaylistServiceImplementation implements SavedPlaylistService {

    private final SavedPlaylistRepository savedPlaylistRepository;
    private final PlaylistPreconditionService playlistPreconditionService;


    @Autowired
    public SavedPlaylistServiceImplementation(
            SavedPlaylistRepository savedPlaylistRepository,
            PlaylistPreconditionService playlistPreconditionService
    ) {
        this.savedPlaylistRepository = savedPlaylistRepository;
        this.playlistPreconditionService = playlistPreconditionService;
    }

    @Override
    @Transactional
    public Long addPlaylist(Long playlistId, DataForToken tokenData) {
        Playlist playlist = this.playlistPreconditionService.getByIdIfExists(playlistId);

        if (!playlist.getAccessLevel().isAvailable())
            throw new ForbiddenException("Playlist (id=" + playlistId + ") is not PUBLIC");

        if (playlist.getUser().getId().equals(tokenData.getEntityId()))
            throw new ConflictException("Playlist (id=" + playlistId + ") can't be saved by it's user-author (id=" + tokenData.getEntityId() + ")");

        try {
            return this.savedPlaylistRepository.save(tokenData.getEntityId(), playlistId);
        }
        catch (DataIntegrityViolationException e) {
            throw new ConflictException("Playlist (id=" + playlistId + ") already saved by user (id=" + tokenData.getEntityId() + ")");
        }
    }

    @Override
    @Transactional
    public void removePlaylist(Long playlistId, DataForToken tokenData) {
        Playlist playlist = this.playlistPreconditionService.getByIdIfExists(playlistId);
        this.savedPlaylistRepository.delete(tokenData.getEntityId(), playlistId);
    }

}
