package ru.smirnov.musicplatform.mapper.abstraction;

import ru.smirnov.musicplatform.dto.domain.track.TrackRequest;
import ru.smirnov.musicplatform.dto.domain.track.TrackResponse;
import ru.smirnov.musicplatform.dto.domain.track.TrackShortcutResponse;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.entity.domain.Track;


public interface TrackMapper {

    Track trackRequestToTrackEntity(TrackRequest dto, Artist artist);

    TrackResponse trackEntityToTrackResponse(Track track);

    TrackShortcutResponse trackEntityToTrackShortcutResponse(Track track, Boolean isSaved);
}
