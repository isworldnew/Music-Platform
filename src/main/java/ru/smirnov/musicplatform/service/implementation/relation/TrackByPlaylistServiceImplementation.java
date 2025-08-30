package ru.smirnov.musicplatform.service.implementation.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.precondition.abstraction.domain.PlaylistPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.domain.TrackPreconditionService;
import ru.smirnov.musicplatform.repository.relation.TrackByPlaylistRepository;
import ru.smirnov.musicplatform.service.abstraction.relation.TrackByPlaylistService;

@Service
public class TrackByPlaylistServiceImplementation implements TrackByPlaylistService {

    private final TrackByPlaylistRepository trackByPlaylistRepository;
    private final TrackPreconditionService trackPreconditionService;
    private final PlaylistPreconditionService playlistPreconditionService;

    @Autowired
    public TrackByPlaylistServiceImplementation(
            TrackByPlaylistRepository trackByPlaylistRepository,
            TrackPreconditionService trackPreconditionService,
            PlaylistPreconditionService playlistPreconditionService
    ) {
        this.trackByPlaylistRepository = trackByPlaylistRepository;
        this.trackPreconditionService = trackPreconditionService;
        this.playlistPreconditionService = playlistPreconditionService;
    }

    @Override
    @Transactional
    public Long addTrack(Long playlistId, Long trackId, DataForToken tokenData) {
        // проверить, что плейлист принадлежит пользователю
        // проверить, что трек сохранён у пользователя
    }

    @Override
    @Transactional
    public void removeTrack(Long playlistId, Long trackId, DataForToken tokenData) {
        // проверить, что плейлист принадлежит пользователю
        // проверить, что трек сохранён у пользователя

    }
}
