package ru.smirnov.musicplatform.service.implementation.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.entity.domain.Track;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.precondition.abstraction.domain.TrackPreconditionService;
import ru.smirnov.musicplatform.repository.audience.UserRepository;
import ru.smirnov.musicplatform.repository.relation.SavedTrackRepository;
import ru.smirnov.musicplatform.repository.relation.TaggedTrackRepository;
import ru.smirnov.musicplatform.service.abstraction.relation.SavedTrackService;

import java.util.List;


// [v] checked
@Service
public class SavedTrackServiceImplementation implements SavedTrackService {

    private final SavedTrackRepository savedTrackRepository;

    private final TrackPreconditionService trackPreconditionService;

    private final TaggedTrackRepository taggedTrackRepository;
    private final UserRepository userRepository;

    @Autowired
    public SavedTrackServiceImplementation(
            SavedTrackRepository savedTrackRepository,
            TrackPreconditionService trackPreconditionService,
            TaggedTrackRepository taggedTrackRepository,
            UserRepository userRepository
    ) {
        this.savedTrackRepository = savedTrackRepository;
        this.trackPreconditionService = trackPreconditionService;
        this.taggedTrackRepository = taggedTrackRepository;
        this.userRepository = userRepository;
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
    public void deleteTrackFromSaved(Long trackId, DataForToken tokenData) {
        User user = this.userRepository.findById(tokenData.getEntityId()).orElseThrow(
                () -> new ForbiddenException("User's business-data wasn't found by users's id in token")
        );
        Track track = this.trackPreconditionService.getByIdIfExists(trackId);
        List<Long> userTags = user.getTags().stream().map(tag -> tag.getId()).toList();

        this.savedTrackRepository.delete(tokenData.getEntityId(), trackId);

        for (Long tagId : userTags) this.taggedTrackRepository.delete(trackId, tagId);
    }

}
