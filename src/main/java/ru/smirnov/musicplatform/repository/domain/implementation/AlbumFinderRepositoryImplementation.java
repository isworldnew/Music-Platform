package ru.smirnov.musicplatform.repository.domain.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionShortcutResponse;
import ru.smirnov.musicplatform.repository.domain.finder.AlbumFinderRepository;

import java.util.List;

@Repository
public class AlbumFinderRepositoryImplementation implements AlbumFinderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MusicCollectionShortcutResponse> searchAlbums(String searchRequest) {

    }

    @Override
    public List<MusicCollectionShortcutResponse> searchSavedAlbums(String searchRequest, Long userId) {
    }
}
