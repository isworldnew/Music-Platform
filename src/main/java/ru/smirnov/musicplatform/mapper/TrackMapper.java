package ru.smirnov.musicplatform.mapper;

import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.domain.track.TrackToCreateDto;
import ru.smirnov.musicplatform.entity.auxiliary.enums.TrackStatus;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.entity.domain.Track;

import java.time.Duration;
import java.time.OffsetDateTime;

@Component
public class TrackMapper {

    public Track createTrackEntity(
            TrackToCreateDto dto,
            Artist artist,
            String coverReference,
            String audioReference
    ) {
        Track track = new Track();
        track.setName(dto.getName());
        track.setGenre(dto.getGenre());
        track.setStatus(TrackStatus.UPLOADED_AND_HIDDEN);
        track.setImageReference(coverReference);
        track.setAudiofileReference(audioReference);
        track.setArtist(artist);
        track.setUploadDateTime(OffsetDateTime.now());
        return track;
    }

}
