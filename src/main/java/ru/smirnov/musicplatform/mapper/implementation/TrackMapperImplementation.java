package ru.smirnov.musicplatform.mapper.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.tmp.ArtistShortcutResponse;
import ru.smirnov.musicplatform.dto.tmp.TrackRequest;
import ru.smirnov.musicplatform.dto.tmp.TrackResponse;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.entity.domain.Track;
import ru.smirnov.musicplatform.mapper.abstraction.ArtistMapper;
import ru.smirnov.musicplatform.mapper.abstraction.TrackMapper;

import java.util.List;

@Component
public class TrackMapperImplementation implements TrackMapper {

    private final ArtistMapper artistMapper;

    @Autowired
    public TrackMapperImplementation(ArtistMapper artistMapper) {
        this.artistMapper = artistMapper;
    }

    @Override
    public Track trackRequestToTrackEntity(TrackRequest dto, Artist artist) {
        Track track = new Track();
        track.setName(dto.getName());
        track.setArtist(artist);
        track.setGenre(dto.getGenre());
        return track;
    }

    @Override
    public TrackResponse trackEntityToTrackResponse(Track track) {

        ArtistShortcutResponse artist = this.artistMapper.artistEntityToArtistShortcutResponse(track.getArtist());
        List<ArtistShortcutResponse> coArtists = track.getCoArtists().stream()
                .map(ca -> ca.getArtist())
                .map(a -> this.artistMapper.artistEntityToArtistShortcutResponse(a))
                .toList();

        TrackResponse trackResponse = new TrackResponse();
        trackResponse.setId(track.getId());
        trackResponse.setName(track.getName());
        trackResponse.setCoverReference(track.getImageReference());
        trackResponse.setAudioReference(track.getAudiofileReference());
        trackResponse.setGenre(track.getGenre());
        trackResponse.setNumberOfPlays(track.getNumberOfPlays());
        trackResponse.setUploadDateTime(track.getUploadDateTime());
        trackResponse.setStatus(track.getStatus().name());
        trackResponse.setArtist(artist);
        trackResponse.setCoArtists(coArtists);
        return trackResponse;
    }
}
