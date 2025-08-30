package ru.smirnov.musicplatform.service.implementation.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.entity.domain.Track;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.precondition.abstraction.domain.TrackPreconditionService;
import ru.smirnov.musicplatform.repository.relation.SavedTrackRepository;
import ru.smirnov.musicplatform.service.abstraction.relation.SavedTrackService;


@Service
public class SavedTrackServiceImplementation implements SavedTrackService {

    private final SavedTrackRepository savedTrackRepository;

    private final TrackPreconditionService trackPreconditionService;

    @Autowired
    public SavedTrackServiceImplementation(
            SavedTrackRepository savedTrackRepository,
            TrackPreconditionService trackPreconditionService
    ) {
        this.savedTrackRepository = savedTrackRepository;
        this.trackPreconditionService = trackPreconditionService;
    }

    @Override
    @Transactional
    public Long saveTrack(Long trackId, DataForToken tokenData) {
        Track track = this.trackPreconditionService.getIfExistsAndPublic(trackId);

        try {
            return this.savedTrackRepository.save(tokenData.getEntityId(), trackId);
        }
        catch (DataIntegrityViolationException e) {
            throw new ConflictException("Track (id=" + trackId + ") already saved by user (id=" + tokenData.getEntityId() + ")");
        }
    }

    @Override
    @Transactional
    public void deleteTrack(Long trackId, DataForToken tokenData) {
        Track track = this.trackPreconditionService.getByIdIfExists(trackId);

        this.savedTrackRepository.delete(tokenData.getEntityId(), trackId);
    }

}
