package ru.smirnov.musicplatform.finder.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistResponse;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.finder.abstraction.ArtistFinderService;
import ru.smirnov.musicplatform.mapper.abstraction.ArtistMapper;
import ru.smirnov.musicplatform.repository.domain.finder.ArtistFinderRepository;

import java.util.List;

@Service
public class ArtistFinderServiceImplementation implements ArtistFinderService {

    private final ArtistFinderRepository artistFinderRepository;
    private final ArtistMapper artistMapper;

    @Autowired
    public ArtistFinderServiceImplementation(ArtistFinderRepository artistFinderRepository, ArtistMapper artistMapper) {
        this.artistFinderRepository = artistFinderRepository;
        this.artistMapper = artistMapper;
    }

    @Override
    public List<ArtistResponse> searchArtists(String searchRequest) {
        List<Artist> artists = this.artistFinderRepository.searchArtists(searchRequest);
        return artists.stream().map(artist -> this.artistMapper.artistEntityToArtistResponse(artist)).toList();
    }

    @Override
    public List<ArtistResponse> searchArtists(String searchRequest, Long distributorId) {
        List<Artist> artists = this.artistFinderRepository.searchArtists(searchRequest, distributorId);
        return artists.stream().map(artist -> this.artistMapper.artistEntityToArtistResponse(artist)).toList();
    }
}
