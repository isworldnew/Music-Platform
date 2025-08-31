package ru.smirnov.musicplatform.service.deprecated.sql.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.projection.CoArtistProjection;
import ru.smirnov.musicplatform.repository.relation.CoArtistRepository;

import java.util.List;

@Service
public class CoArtistServiceOld {

    private final CoArtistRepository coArtistRepository;

    @Autowired
    public CoArtistServiceOld(CoArtistRepository coArtistRepository) {
        this.coArtistRepository = coArtistRepository;
    }

    public Long save(Long trackId, Long artistId) {
        return this.coArtistRepository.save(trackId, artistId);
    }

    public List<CoArtistProjection> getCoArtistProjections(Long trackId) {
        return this.coArtistRepository.getCoArtistProjections(trackId);
    }

}
