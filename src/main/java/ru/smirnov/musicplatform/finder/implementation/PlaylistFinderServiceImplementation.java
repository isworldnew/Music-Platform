package ru.smirnov.musicplatform.finder.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.finder.abstraction.PlaylistFinderService;
import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;
import ru.smirnov.musicplatform.projection.implementation.MusicCollectionShortcutProjectionImplementation;
import ru.smirnov.musicplatform.repository.domain.finder.PlaylistFinderRepository;

import java.util.List;

@Service
public class PlaylistFinderServiceImplementation implements PlaylistFinderService {

    private final PlaylistFinderRepository playlistFinderRepository;

    @Autowired
    public PlaylistFinderServiceImplementation(PlaylistFinderRepository playlistFinderRepository) {
        this.playlistFinderRepository = playlistFinderRepository;
    }

    @Override
    public List<MusicCollectionShortcutProjection> searchPlaylists(String searchRequest, Long userId, boolean savedOnly) {
        List<MusicCollectionShortcutProjection> playlists = this.playlistFinderRepository.searchPlaylists(searchRequest, userId, savedOnly);

        for (MusicCollectionShortcutProjection playlist : playlists) {
            if (!playlist.getAccessLevel().isAvailable()) {
                ((MusicCollectionShortcutProjectionImplementation) playlist).setImageReference(null);
            }
        }

        return playlists;
    }

    @Override
    public List<MusicCollectionShortcutProjection> getOwnedPlaylists(Long userId) {
        return this.playlistFinderRepository.getOwnedPlaylists(userId);
    }

    @Override
    public List<MusicCollectionShortcutProjection> getSavedPlaylists(Long userId) {
        List<MusicCollectionShortcutProjection> playlists = this.playlistFinderRepository.getSavedPlaylists(userId);

        for (MusicCollectionShortcutProjection playlist : playlists) {
            if (!playlist.getAccessLevel().isAvailable()) {
                ((MusicCollectionShortcutProjectionImplementation) playlist).setImageReference(null);
            }
        }

        return playlists;
    }
}
