package ru.smirnov.musicplatform.precondition.implementation.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.entity.domain.Playlist;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.exception.NotFoundException;
import ru.smirnov.musicplatform.precondition.abstraction.domain.PlaylistPreconditionService;
import ru.smirnov.musicplatform.repository.domain.PlaylistRepository;

@Service
public class PlaylistPreconditionServiceImplementation implements PlaylistPreconditionService {

    private final PlaylistRepository playlistRepository;

    @Autowired
    public PlaylistPreconditionServiceImplementation(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    @Override
    public void existsByNameAndUserId(String name, Long userId) {
        if (this.playlistRepository.findByNameAndUserId(name, userId).isPresent())
            throw new ConflictException("User (id=" + userId + ") already has playlist with name='" + name + "')");
    }

    @Override
    public Playlist getByIdIfExists(Long playlistId) {
        return this.playlistRepository.findById(playlistId).orElseThrow(
                () -> new NotFoundException("Playlist with id=" + playlistId + " was not found")
        );
    }

    @Override
    public Playlist existsAndBelongToUser(Long playlistId, Long userId) {
        Playlist playlist = this.getByIdIfExists(playlistId);

        if (!playlist.getUser().getId().equals(userId))
            throw new ForbiddenException("User (id=" + userId + ") has no rights to manage playlist (id=" + playlistId + ")");

        return playlist;
    }

    @Override
    public Playlist getByIdIfExistsAndNameIsUnique(Long playlistId, Long userId, String name) {
        Playlist playlistFoundById = this.existsAndBelongToUser(playlistId, userId);

        if (playlistFoundById.getName().equals(name))
            return playlistFoundById;

        Playlist playlistFoundByName = this.playlistRepository.findByNameAndUserId(name, userId).orElse(null);

        if (playlistFoundByName != null)
            throw new ConflictException("User (id=" + userId + ") already has playlist (id=" + playlistFoundByName.getId() + ") with name='" + name + "'");

        return playlistFoundById;
    }
}
