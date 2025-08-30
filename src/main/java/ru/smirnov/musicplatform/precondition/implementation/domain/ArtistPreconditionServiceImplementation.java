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
    public Artist getByIdIfExists(Long artistId) {
        return this.artistRepository.findById(artistId).orElseThrow(
                () -> new NotFoundException("Artist with id=" + artistId + " was not found")
        );
    }

    @Override
    public Artist getByIdIfExistsAndNameIsUnique(Long artistId, String name) {
        /*
        Комплексная проверка:
        [v] Исполнитель с таким id существует
        [v] Переданное имя принадлежит либо ему (то есть оно не обновилось)
        [v] Либо переданное имя уникально среди исполнителей
        */
        Artist artistFoundById = this.getByIdIfExists(artistId);

        if (artistFoundById.getName().equals(name))
            return artistFoundById;

        // второй запрос и не понадобится, если имена совпали
        Artist artistFoundByName = this.artistRepository.findByName(name).orElse(null);

        if (artistFoundByName != null/*&& (!artistFoundByName.getId().equals(artistId))*/)
            throw new ConflictException("Artist with such name already exists");

        return artistFoundById;
    }

}
