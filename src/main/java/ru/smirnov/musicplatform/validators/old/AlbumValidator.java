package ru.smirnov.musicplatform.validators.old;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.exception.NotFoundException;
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

    public Album safelyGetById(Long albumId) {

        Album album = this.albumRepository.findById(albumId).orElse(null);

        if (album == null)
            throw new NotFoundException("No album found with id=" + albumId);

        return album;

    }

    public void checkAlbumNameUniquenessForArtistDuringUpdate(String name, Long artistId, Long albumId) {

        // во время обновления альбома может прийти то же самое название, а может прийти новое
        // если по такому названию у исполнителя ещё нет альбомов - ок
        // если альбом с таким названием уже есть, то нужно проверить, это тот альбом, который мы обновляем, или другой
        // если тот же самый - ок

        Album album = this.albumRepository.findByNameAndArtistId(name, artistId).orElse(null);

        if (album != null) {
            if (!albumId.equals(album.getId()))
                throw new ConflictException("Album name '" + name + "' for artist with id=" + artistId + " already exists");
        }

    }

}
