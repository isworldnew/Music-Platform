package ru.smirnov.musicplatform.service.implementation.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.entity.domain.Tag;
import ru.smirnov.musicplatform.entity.domain.Track;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.precondition.abstraction.domain.TagPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.domain.TrackPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.relation.SavedTrackPreconditionService;
import ru.smirnov.musicplatform.repository.relation.TaggedTrackRepository;
import ru.smirnov.musicplatform.service.abstraction.relation.TaggedTrackService;

// [v] checked
@Service
public class TaggedTrackServiceImplementation implements TaggedTrackService {

    private final TaggedTrackRepository taggedTrackRepository;

    private final TrackPreconditionService trackPreconditionService;
    private final SavedTrackPreconditionService savedTrackPreconditionService;
    private final TagPreconditionService tagPreconditionService;

    @Autowired
    public TaggedTrackServiceImplementation(
            TaggedTrackRepository taggedTrackRepository,
            TrackPreconditionService trackPreconditionService,
            SavedTrackPreconditionService savedTrackPreconditionService,
            TagPreconditionService tagPreconditionService
    ) {
        this.taggedTrackRepository = taggedTrackRepository;
        this.trackPreconditionService = trackPreconditionService;
        this.savedTrackPreconditionService = savedTrackPreconditionService;
        this.tagPreconditionService = tagPreconditionService;
    }

    @Override
    @Transactional
    public Long tagTrack(Long trackId, Long tagId, DataForToken tokenData) {
        Track track = this.trackPreconditionService.getByIdIfExists(trackId);
        this.savedTrackPreconditionService.trackIsSavedCheck(trackId, tokenData.getEntityId());
        Tag tag = this.tagPreconditionService.getByIdIfExistsAndBelongsToUser(tagId, tokenData.getEntityId());
        try {
            return this.taggedTrackRepository.save(trackId, tagId);
        }
        catch (DataIntegrityViolationException e) {
            throw new ConflictException("Track (id=" + trackId + ") already tagged by tag (id=" + tagId + ")");
        }
    }

    @Override
    @Transactional
    public void unTagTrack(Long trackId, Long tagId, DataForToken tokenData) {
        Track track = this.trackPreconditionService.getByIdIfExists(trackId);
        Tag tag = this.tagPreconditionService.getByIdIfExistsAndBelongsToUser(tagId, tokenData.getEntityId());
        this.taggedTrackRepository.delete(trackId, tagId);
    }
}
