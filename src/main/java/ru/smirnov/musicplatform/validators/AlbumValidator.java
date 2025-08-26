package ru.smirnov.musicplatform.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.repository.domain.AlbumRepository;

@Service
public class AlbumValidator {

    private final AlbumRepository albumRepository;

    @Autowired
    public AlbumValidator(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public void checkAlbumNameUniquenessForArtist(String name, Long artistId) {

        Album album = this.albumRepository.findByNameAndArtistId(name, artistId).orElse(null);

        if (album != null)
            throw new ConflictException("Album name '" + name + "' for artist with id=" + artistId + " already exists");

    }

}
