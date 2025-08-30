package ru.smirnov.musicplatform.precondition.implementation.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.precondition.abstraction.relation.SavedTrackPreconditionService;
import ru.smirnov.musicplatform.repository.relation.SavedTrackRepository;

@Service
public class SavedTrackPreconditionServiceServiceImplementation implements SavedTrackPreconditionService {

    private final SavedTrackRepository savedTrackRepository;

    @Autowired
    public SavedTrackPreconditionServiceServiceImplementation(SavedTrackRepository savedTrackRepository) {
        this.savedTrackRepository = savedTrackRepository;
    }

    @Override
    public void trackIsSavedCheck(Long trackId, Long userId) {
        this.savedTrackRepository.findByTrackIdAndUserId(trackId, userId).orElseThrow(
                () -> new ForbiddenException("Track (id=" + trackId + ") is not saved by user (id=" + userId + ")")
        );
    }

}
