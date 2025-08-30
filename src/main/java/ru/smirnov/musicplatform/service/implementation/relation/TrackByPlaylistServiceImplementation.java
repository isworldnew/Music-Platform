package ru.smirnov.musicplatform.service.implementation.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.entity.domain.Playlist;
import ru.smirnov.musicplatform.entity.domain.Track;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.precondition.abstraction.domain.PlaylistPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.domain.TrackPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.relation.SavedTrackPreconditionService;
import ru.smirnov.musicplatform.repository.relation.TrackByPlaylistRepository;
import ru.smirnov.musicplatform.service.abstraction.relation.TrackByPlaylistService;

@Service
public class TrackByPlaylistServiceImplementation implements TrackByPlaylistService {

    private final TrackByPlaylistRepository trackByPlaylistRepository;
    private final TrackPreconditionService trackPreconditionService;

    private final PlaylistPreconditionService playlistPreconditionService;
    private final SavedTrackPreconditionService savedTrackPreconditionService;

    @Autowired
    public TrackByPlaylistServiceImplementation(
            TrackByPlaylistRepository trackByPlaylistRepository,
            TrackPreconditionService trackPreconditionService,
            PlaylistPreconditionService playlistPreconditionService,
            SavedTrackPreconditionService savedTrackPreconditionService,
    ) {
        this.trackByPlaylistRepository = trackByPlaylistRepository;
        this.trackPreconditionService = trackPreconditionService;
        this.playlistPreconditionService = playlistPreconditionService;
        this.savedTrackPreconditionService = savedTrackPreconditionService;
    }

    @Override
    @Transactional
    public Long addTrack(Long playlistId, Long trackId, DataForToken tokenData) {
        Playlist playlist = this.playlistPreconditionService.existsAndBelongToUser(playlistId, tokenData.getEntityId());
        Track track = this.trackPreconditionService.getIfExistsAndPublic(trackId);
        this.savedTrackPreconditionService.trackIsSavedCheck(trackId, tokenData.getEntityId());

        try {
            return this.trackByPlaylistRepository.save(playlistId, trackId);
        }
        catch (DataIntegrityViolationException e) {
            throw new ConflictException("Track (id=" + trackId + ") already exists in playlist (id=" + playlistId + ")");
        }
    }

    @Override
    @Transactional
    public void removeTrack(Long playlistId, Long trackId, DataForToken tokenData) {
        если удалить сохранённый трек - он же в плейлистах пользователя останется
        и в тегах тоже...
    }
}
