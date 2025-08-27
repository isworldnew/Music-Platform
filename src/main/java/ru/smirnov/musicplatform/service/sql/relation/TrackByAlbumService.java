package ru.smirnov.musicplatform.service.sql.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.repository.relation.TrackByAlbumRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrackByAlbumService {

    private final TrackByAlbumRepository trackByAlbumRepository;

    @Autowired
    public TrackByAlbumService(TrackByAlbumRepository trackByAlbumRepository) {
        this.trackByAlbumRepository = trackByAlbumRepository;
    }

    // предполагается, что метод этот вызывается из AlbumService
    // и что все необходимые предварительные проверки пройдены
    @Transactional
    public List<Long> addTracksToAlbum(Long albumId, List<Long> tracksToAdd) {

        List<Long> relations = new ArrayList<>();

        for (Long trackId : tracksToAdd) {
            try {
                relations.add(this.trackByAlbumRepository.save(albumId, trackId));
            }
            catch (DataIntegrityViolationException e) {
                throw new ConflictException("Track with id=" + trackId + " already exists in album with id=" + albumId);
            }
        }

        return relations;
    }

    // предполагается, что метод этот вызывается из AlbumService
    // и что все необходимые предварительные проверки пройдены
    @Transactional
    public void removeTracksFromAlbum(Long albumId, List<Long> tracksToRemove) {
        tracksToRemove.forEach(trackToRemove -> this.trackByAlbumRepository.delete(albumId, trackToRemove));
    }

}
