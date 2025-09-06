package ru.smirnov.musicplatform.mapper.abstraction;

import ru.smirnov.musicplatform.dto.domain.tag.TagResponse;
import ru.smirnov.musicplatform.dto.domain.track.TrackExtendedResponse;
import ru.smirnov.musicplatform.dto.domain.track.TrackRequest;
import ru.smirnov.musicplatform.dto.domain.track.TrackResponse;
import ru.smirnov.musicplatform.dto.domain.track.TrackShortcutResponse;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.entity.domain.Track;

import java.util.List;


public interface TrackMapper {

    Track trackRequestToTrackEntity(TrackRequest dto, Artist artist);

    TrackResponse trackEntityToTrackResponse(Track track);

    TrackExtendedResponse trackEntityToTrackExtendedResponse(Track track, List<TagResponse> tags);

    TrackShortcutResponse trackEntityToTrackShortcutResponse(Track track, Boolean isSaved);
}
