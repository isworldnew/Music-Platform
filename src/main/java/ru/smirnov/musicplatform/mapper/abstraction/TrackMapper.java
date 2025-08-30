package ru.smirnov.musicplatform.mapper.abstraction;

import ru.smirnov.musicplatform.dto.tmp.TrackRequest;
import ru.smirnov.musicplatform.dto.tmp.TrackResponse;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.entity.domain.Track;


public interface TrackMapper {

    Track trackRequestToTrackEntity(TrackRequest dto, Artist artist);

    TrackResponse trackEntityToTrackResponse(Track track);
}
