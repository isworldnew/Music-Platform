package ru.smirnov.musicplatform.precondition.implementation.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.exception.NotFoundException;
import ru.smirnov.musicplatform.precondition.abstraction.domain.AlbumPreconditionService;
import ru.smirnov.musicplatform.repository.domain.AlbumRepository;

@Service
public class AlbumPreconditionServiceImplementation implements AlbumPreconditionService {

    private final AlbumRepository albumRepository;

    @Autowired
    public AlbumPreconditionServiceImplementation(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    @Override
    public void existsByNameAndArtistId(String name, Long artistId) {
        Album album = this.albumRepository.findByNameAndArtistId(name, artistId).orElse(null);

        if (album != null)
            throw new ConflictException("Artist (id=" + artistId + ") already has album (id=" + album.getId() + ") with name='" + name + "'");
    }

    @Override
    public Album getByIdIfExists(Long albumId) {
        return this.albumRepository.findById(albumId).orElseThrow(
                () -> new NotFoundException("Album with id=" + albumId + " was not found")
        );
    }

    @Override
    public Album getByIdIfExistsAndNameIsUnique(Long albumId, String name) {
        Album albumFoundById = this.getByIdIfExists(albumId);

        if (albumFoundById.getName().equals(name)) // значит: имя либо принадлежит другому альбому, либо никакому
            return albumFoundById;

        Album albumFoundByName = this.albumRepository.findByNameAndArtistId(name, albumFoundById.getArtist().getId()).orElse(null);

        if (albumFoundByName != null)
            throw new ConflictException("Artist (id=" + albumFoundById.getArtist().getId() + ") already has album (id=" + albumFoundByName.getId() + ") with name='" + name + "'");

        return albumFoundById;
    }

}
