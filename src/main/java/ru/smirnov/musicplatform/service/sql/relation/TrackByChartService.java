package ru.smirnov.musicplatform.service.sql.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.repository.relation.TrackByChartRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrackByChartService {

    private final TrackByChartRepository trackByChartRepository;

    @Autowired
    public TrackByChartService(TrackByChartRepository trackByChartRepository) {
        this.trackByChartRepository = trackByChartRepository;
    }

    @Transactional
    public List<Long> save(Long chartId, List<Long> tracksToAdd) {
        // нужно предварительно проверить существование каждого трека
        List<Long> relations = new ArrayList<>();

        for (Long trackId : tracksToAdd) {
            try {
                relations.add(this.trackByChartRepository.save(chartId, trackId));
            }
            catch (DataIntegrityViolationException e) {
                throw new ConflictException("Track with id=" + trackId + " already exists in chart with id=" + chartId);
            }
        }

        return relations;
    }

    public void remove(Long chartId, List<Long> tracksToRemove) {
        for (Long trackId : tracksToRemove)
            this.trackByChartRepository.delete(chartId, trackId);
    }
}
