package ru.smirnov.musicplatform.mapper.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistShortcutResponse;
import ru.smirnov.musicplatform.dto.domain.tag.TagResponse;
import ru.smirnov.musicplatform.dto.domain.track.TrackExtendedResponse;
import ru.smirnov.musicplatform.dto.domain.track.TrackRequest;
import ru.smirnov.musicplatform.dto.domain.track.TrackResponse;
import ru.smirnov.musicplatform.dto.domain.track.TrackShortcutResponse;
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

        TrackResponse trackResponse = new TrackResponse();

        ArtistShortcutResponse artist = this.artistMapper.artistEntityToArtistShortcutResponse(track.getArtist());

        trackResponse.setId(track.getId());
        trackResponse.setName(track.getName());
        trackResponse.setCoverReference(track.getImageReference());
        trackResponse.setAudioReference(track.getAudiofileReference());
        trackResponse.setGenre(track.getGenre());
        trackResponse.setNumberOfPlays(track.getNumberOfPlays());
        trackResponse.setUploadDateTime(track.getUploadDateTime());
        trackResponse.setStatus(track.getStatus().name());
        trackResponse.setArtist(artist);

        if (track.getCoArtists() != null && !track.getCoArtists().isEmpty()) {
            List<ArtistShortcutResponse> coArtists = track.getCoArtists().stream()
                    .map(ca -> ca.getArtist())
                    .map(a -> this.artistMapper.artistEntityToArtistShortcutResponse(a))
                    .toList();

            trackResponse.setCoArtists(coArtists);
        }

        return trackResponse;
    }

    @Override
    public TrackShortcutResponse trackEntityToTrackShortcutResponse(Track track, Boolean isSaved) {
        ArtistShortcutResponse artist = new ArtistShortcutResponse(track.getArtist().getId(), track.getArtist().getName());
        List<ArtistShortcutResponse> coArtists = track.getCoArtists().stream()
                .map(coArtist -> new ArtistShortcutResponse(coArtist.getArtist().getId(), coArtist.getArtist().getName()))
                .toList();

        TrackShortcutResponse dto = new TrackShortcutResponse();
        dto.setId(track.getId());
        dto.setName(track.getName());
        dto.setCoverReference(track.getImageReference());
        dto.setAudioReference(track.getAudiofileReference());
        dto.setGenre(track.getGenre());
        dto.setStatus(track.getStatus().name());
        dto.setSaved(isSaved);
        dto.setArtist(artist);
        dto.setCoArtists(coArtists);

        if (!track.getStatus().isAvailable()) {
            dto.setCoverReference(null);
            dto.setAudioReference(null);
        }

        return dto;
    }

    @Override
    public TrackExtendedResponse trackEntityToTrackExtendedResponse(Track track, List<TagResponse> tags) {

        TrackExtendedResponse trackResponse = new TrackExtendedResponse();

        ArtistShortcutResponse artist = this.artistMapper.artistEntityToArtistShortcutResponse(track.getArtist());

        trackResponse.setId(track.getId());
        trackResponse.setName(track.getName());
        trackResponse.setCoverReference(track.getImageReference());
        trackResponse.setAudioReference(track.getAudiofileReference());
        trackResponse.setGenre(track.getGenre());
        trackResponse.setNumberOfPlays(track.getNumberOfPlays());
        trackResponse.setUploadDateTime(track.getUploadDateTime());
        trackResponse.setStatus(track.getStatus().name());
        trackResponse.setArtist(artist);
        trackResponse.setTags(tags);

        if (track.getCoArtists() != null && !track.getCoArtists().isEmpty()) {
            List<ArtistShortcutResponse> coArtists = track.getCoArtists().stream()
                    .map(ca -> ca.getArtist())
                    .map(a -> this.artistMapper.artistEntityToArtistShortcutResponse(a))
                    .toList();

            trackResponse.setCoArtists(coArtists);
        }

        return trackResponse;
    }
}
