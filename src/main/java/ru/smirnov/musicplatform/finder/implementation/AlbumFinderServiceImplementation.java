package ru.smirnov.musicplatform.finder.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.finder.abstraction.AlbumFinderService;
import ru.smirnov.musicplatform.mapper.abstraction.AlbumMapper;
import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;
import ru.smirnov.musicplatform.projection.implementation.MusicCollectionShortcutProjectionImplementation;
import ru.smirnov.musicplatform.repository.audience.UserRepository;
import ru.smirnov.musicplatform.repository.domain.finder.AlbumFinderRepository;

import java.util.List;

@Service
public class AlbumFinderServiceImplementation implements AlbumFinderService {

    private final AlbumFinderRepository albumFinderRepository;

    @Autowired
    public AlbumFinderServiceImplementation(AlbumFinderRepository albumFinderRepository) {
        this.albumFinderRepository = albumFinderRepository;
    }

    @Override
    public List<MusicCollectionShortcutProjection> searchAlbums(String searchRequest, Long userId, boolean savedOnly) {

        List<MusicCollectionShortcutProjection> albums = this.albumFinderRepository.searchAlbums(searchRequest, userId, savedOnly);

        for (MusicCollectionShortcutProjection album : albums) {
            if (!album.getAccessLevel().isAvailable()) {
                ((MusicCollectionShortcutProjectionImplementation) album).setImageReference(null);
            }
        }

        return albums;
    }

    @Override
    public List<MusicCollectionShortcutProjection> getSavedAlbums(Long userId) {

        List<MusicCollectionShortcutProjection> albums = this.albumFinderRepository.getSavedAlbums(userId);

        for (MusicCollectionShortcutProjection album : albums) {
            if (!album.getAccessLevel().isAvailable()) {
                ((MusicCollectionShortcutProjectionImplementation) album).setImageReference(null);
            }
        }

        return albums;
    }

}
