package ru.smirnov.musicplatform.service.abstraction.relation;

public interface CoArtistService {

    Long addCoArtistsToTrack(Long trackId, CoArtistRequest dto);

}
