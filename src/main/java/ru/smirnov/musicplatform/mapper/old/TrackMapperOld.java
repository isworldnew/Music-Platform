package ru.smirnov.musicplatform.mapper.old;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistShortcutDto;
import ru.smirnov.musicplatform.dto.domain.track.TrackDataDto;
import ru.smirnov.musicplatform.dto.domain.track.TrackShortcutDto;
import ru.smirnov.musicplatform.dto.domain.track.TrackToCreateDto;
import ru.smirnov.musicplatform.entity.auxiliary.enums.TrackStatus;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.entity.domain.Track;
import ru.smirnov.musicplatform.projection.CoArtistProjection;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class TrackMapperOld {

    private final ArtistMapperOld artistMapper;

    @Autowired
    public TrackMapperOld(ArtistMapperOld artistMapper) {
        this.artistMapper = artistMapper;
    }

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

    public TrackDataDto trackEntityToTrackDataDto(Track track, List<CoArtistProjection> coArtists, boolean safeUpload) {
        TrackDataDto dto = new TrackDataDto();
        dto.setId(track.getId());
        dto.setName(track.getName());
        dto.setArtist(new TrackDataDto.ArtistData(track.getArtist().getId(), track.getArtist().getName()));
        dto.setCoArtists(coArtists);
        dto.setGenre(track.getGenre());
        dto.setCoverReference(track.getImageReference());
        dto.setAudioReference(track.getAudiofileReference());
        dto.setNumberOfPlays(track.getNumberOfPlays());
        dto.setStatus(track.getStatus().name());
        dto.setUploadDateTime(track.getUploadDateTime());

        if (safeUpload) {
            if (!track.getStatus().isAvailable()) {
                dto.setCoverReference(null);
                dto.setAudioReference(null);
            }
        }

        return dto;
    }

    public TrackShortcutDto trackEntityToTrackShortcutDto(Track track) {
        ArtistShortcutDto artist = this.artistMapper.artistEntityToArtistShortcutDto(track.getArtist());
        List<ArtistShortcutDto> coArtists = track.getCoArtists().stream()
                .map(ca -> ca.getArtist())
                .map(a -> this.artistMapper.artistEntityToArtistShortcutDto(a))
                .toList();

        TrackShortcutDto dto = new TrackShortcutDto();
        dto.setId(track.getId());
        dto.setName(track.getName());
        dto.setArtist(artist);
        dto.setCoArtists(coArtists);
        dto.setCoverReference(track.getImageReference());
        dto.setStatus(track.getStatus().name());
        return dto;
    }


}
