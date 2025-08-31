package ru.smirnov.musicplatform.service.deprecated.sql.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.repository.relation.TrackByPlaylistRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrackByPlaylistServiceOld {

    private final TrackByPlaylistRepository trackByPlaylistRepository;

    @Autowired
    public TrackByPlaylistServiceOld(TrackByPlaylistRepository trackByPlaylistRepository) {
        this.trackByPlaylistRepository = trackByPlaylistRepository;
    }

    @Transactional
    public List<Long> save(Long playlistId, List<Long> tracksToAdd) {
        // нужно предварительно проверить существование каждого трека
        List<Long> relations = new ArrayList<>();

        for (Long trackId : tracksToAdd) {
            try {
                relations.add(this.trackByPlaylistRepository.save(playlistId, trackId));
            }
            catch (DataIntegrityViolationException e) {
                throw new ConflictException("Track with id=" + trackId + " already exists in playlist with id=" + playlistId);
            }
        }

        return relations;
    }

    @Transactional
    public void remove(Long playlistId, List<Long> tracksToRemove) {
        for (Long trackId : tracksToRemove)
            this.trackByPlaylistRepository.delete(playlistId, trackId);
    }

}
