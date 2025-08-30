package ru.smirnov.musicplatform.precondition.implementation.domain;

import org.springframework.beans.factory.annotation.Autowired;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.exception.NotFoundException;
import ru.smirnov.musicplatform.precondition.abstraction.domain.ArtistPreconditionService;
import ru.smirnov.musicplatform.repository.domain.ArtistRepository;

public class ArtistPreconditionServiceImplementation implements ArtistPreconditionService {

    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistPreconditionServiceImplementation(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public void existsByName(String name) {
        if (this.artistRepository.findByName(name).isPresent())
            throw new ConflictException("Artist with such name already exists");
    }

    @Override
    public Artist safelyGetById(Long artistId) {
        return this.artistRepository.findById(artistId).orElseThrow(
                () -> new NotFoundException("Artist with id=" + artistId + " was not found")
        );
    }


}
