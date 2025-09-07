package ru.smirnov.musicplatform.finder.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionResponse;
import ru.smirnov.musicplatform.entity.auxiliary.enums.Role;
import ru.smirnov.musicplatform.entity.domain.Playlist;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.finder.abstraction.PlaylistFinderService;
import ru.smirnov.musicplatform.mapper.abstraction.PlaylistMapper;
import ru.smirnov.musicplatform.precondition.abstraction.domain.PlaylistPreconditionService;
import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;
import ru.smirnov.musicplatform.projection.abstraction.TrackShortcutProjection;
import ru.smirnov.musicplatform.projection.implementation.MusicCollectionShortcutProjectionImplementation;
import ru.smirnov.musicplatform.repository.domain.PlaylistRepository;
import ru.smirnov.musicplatform.repository.domain.finder.PlaylistFinderRepository;
import ru.smirnov.musicplatform.repository.domain.finder.TrackFinderRepository;

import java.util.List;

@Service
public class PlaylistFinderServiceImplementation implements PlaylistFinderService {

    private final PlaylistFinderRepository playlistFinderRepository;
    private final PlaylistPreconditionService playlistPreconditionService;
    private final PlaylistRepository playlistRepository;
    private final TrackFinderRepository trackFinderRepository;
    private final PlaylistMapper playlistMapper;

    @Autowired
    public PlaylistFinderServiceImplementation(
            PlaylistFinderRepository playlistFinderRepository,
            PlaylistPreconditionService playlistPreconditionService,
            PlaylistRepository playlistRepository,
            TrackFinderRepository trackFinderRepository,
            PlaylistMapper playlistMapper
    ) {
        this.playlistFinderRepository = playlistFinderRepository;
        this.playlistPreconditionService = playlistPreconditionService;
        this.playlistRepository = playlistRepository;
        this.trackFinderRepository = trackFinderRepository;
        this.playlistMapper = playlistMapper;
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

    @Override
    @Transactional
    public MusicCollectionResponse getPlaylistById(Long playlistId, DataForToken tokenData) {
        // для USER (плейлист свой или чужой) и GUEST
        Playlist playlist = this.playlistPreconditionService.getByIdIfExists(playlistId);
        List<TrackShortcutProjection> tracks;

        if (tokenData.getRole().equals(Role.USER.name())) {

            if (!playlist.getUser().getId().equals(tokenData.getEntityId()) && !playlist.getAccessLevel().isAvailable())
                throw new ForbiddenException("Playlist (id=" + playlistId + ") is not PUBLIC");

            if (!playlist.getUser().getId().equals(tokenData.getEntityId())) {
                playlist.setNumberOfPlays(playlist.getNumberOfPlays() + 1);
                this.playlistRepository.save(playlist);
            }

            tracks = this.trackFinderRepository.getTracksByPlaylist(playlistId, true);
            return this.playlistMapper.playlistEntityToMusicCollectionResponse(playlist, tracks);
        }

        if (playlist.getAccessLevel().isAvailable())
            throw new ForbiddenException("Playlist (id=" + playlistId + ") is not PUBLIC");

        playlist.setNumberOfPlays(playlist.getNumberOfPlays() + 1);
        this.playlistRepository.save(playlist);

        tracks = this.trackFinderRepository.getTracksByPlaylist(playlistId, true);
        return this.playlistMapper.playlistEntityToMusicCollectionResponse(playlist, tracks);
    }
}
