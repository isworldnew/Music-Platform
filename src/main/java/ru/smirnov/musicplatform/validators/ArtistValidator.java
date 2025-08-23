package ru.smirnov.musicplatform.validators;

import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.exception.ArtistNameNonUniqueException;
import ru.smirnov.musicplatform.exception.NotFoundException;
import ru.smirnov.musicplatform.repository.domain.ArtistRepository;

@Component
public class ArtistValidator {

    private final ArtistRepository artistRepository;

    public ArtistValidator(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public Artist safelyGetById(Long id) {
        Artist artist = this.artistRepository.findById(id).orElse(null);

        if (artist == null) throw new NotFoundException("Artist with this id was not found");

        return artist;
    }

    // данный метод именно для случаев, когда хочу проверить, что исполнителя c
    // таким именем не существует, чтобы безопасно совершать дальнейшие действия,
    // а не для случаев поиска по имени
    public void existsByName(String name) {
        if (this.artistRepository.findByName(name).isPresent())
            throw new ArtistNameNonUniqueException("Such artist's name already exists");
    }

    // данный метод именно для случаев обновления данных об исполнителе:
    // когда может прийти то же самое имя - надо просто убедиться, что
    // оно принадлежит тому же исполнителю
    public void existsByNameForUpdate(Long id, String name) {
        Artist artist = this.artistRepository.findByName(name).orElse(null);
        if (artist != null && !artist.getId().equals(id))
            throw new ArtistNameNonUniqueException("Such artist's name already exists");
    }


}
