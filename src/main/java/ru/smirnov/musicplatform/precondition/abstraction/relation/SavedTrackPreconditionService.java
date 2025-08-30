package ru.smirnov.musicplatform.precondition.abstraction.relation;

public interface SavedTrackPreconditionService {

    void trackIsSavedCheck(Long trackId, Long userId);

}
