package ru.smirnov.musicplatform.validators;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.entity.domain.Playlist;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.exception.NotFoundException;
import ru.smirnov.musicplatform.repository.domain.PlaylistRepository;

import java.util.List;

@Component
public class PlaylistValidator {

    private final PlaylistRepository playlistRepository;

    @Autowired
    public PlaylistValidator(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public Playlist getPlaylistByIdSafely(Long playlistId) {
        return this.playlistRepository.findById(playlistId).orElseThrow(
                () -> new NotFoundException("Playlist with id=" + playlistId + " was not found")
        );
    }

    public Playlist getPlaylistBelongingToUser(Long playlistId, Long userId) {

        Playlist playlist = this.getPlaylistByIdSafely(playlistId);

        if (!playlist.getUser().getId().equals(userId))
            throw new ForbiddenException("User with id=" + userId + " has no rights to manage playlist with id=" + playlistId);

        return playlist;
    }

    public Playlist getPlaylistBelongingToUserForUpdate(Long playlistId, Long userId, String name) {

        Playlist playlist = this.getPlaylistBelongingToUser(playlistId, userId);

        List<Playlist> userPlaylists = this.playlistRepository.findAllByUserId(userId);

        Playlist targetPlaylist = userPlaylists.stream()
                .filter(userPlaylist -> userPlaylist.getName().equals(name))
                .findFirst().orElse(null);

        if (targetPlaylist != null && !targetPlaylist.getId().equals(playlistId))
            throw new ForbiddenException(
                    "For user with id=" + userId + " the playlist name '" + name + "' already in use by playlist with id=" + targetPlaylist.getId()
            );

        return playlist;
    }

    public void checkPlaylistExistenceByNameAndUserId(String name, Long userId) {
        List<Playlist> playlists = this.playlistRepository.findAllByUserId(userId);

        Playlist targetPlaylist = playlists.stream()
                .filter(playlist -> userId.equals(playlist.getUser().getId()))
                .findFirst().orElse(null);

        if (targetPlaylist != null)
            throw new ConflictException("Playlist with name '" + name + "' already exists for user with id=" + userId);
    }
}
