package ru.smirnov.musicplatform.service.abstraction.relation;

import ru.smirnov.musicplatform.authentication.DataForToken;

public interface TaggedTrackService {

    Long tagTrack(Long trackId, Long tagId, DataForToken tokenData);

    void unTagTrack(Long trackId, Long tagId, DataForToken tokenData);

}
