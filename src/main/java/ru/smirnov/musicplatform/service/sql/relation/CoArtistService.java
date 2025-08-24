package ru.smirnov.musicplatform.service.sql.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.repository.relation.CoArtistRepository;

@Service
public class CoArtistService {

    private final CoArtistRepository coArtistRepository;

    @Autowired
    public CoArtistService(CoArtistRepository coArtistRepository) {
        this.coArtistRepository = coArtistRepository;
    }

    public Long save(Long trackId, Long artistId) {
        return this.coArtistRepository.save(trackId, artistId);
    }
}
