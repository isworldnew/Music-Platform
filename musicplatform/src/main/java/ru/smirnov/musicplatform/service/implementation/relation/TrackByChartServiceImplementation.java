package ru.smirnov.musicplatform.service.implementation.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.entity.domain.Chart;
import ru.smirnov.musicplatform.entity.domain.Track;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.precondition.abstraction.domain.ChartPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.domain.TrackPreconditionService;
import ru.smirnov.musicplatform.repository.relation.TrackByChartRepository;
import ru.smirnov.musicplatform.service.abstraction.relation.TrackByChartService;

// [v] checked
@Service
public class TrackByChartServiceImplementation implements TrackByChartService {

    private final TrackByChartRepository trackByChartRepository;

    private final TrackPreconditionService trackPreconditionService;
    private final ChartPreconditionService chartPreconditionService;

    @Autowired
    public TrackByChartServiceImplementation(
            TrackByChartRepository trackByChartRepository,
            TrackPreconditionService trackPreconditionService,
            ChartPreconditionService chartPreconditionService
    ) {
        this.trackByChartRepository = trackByChartRepository;
        this.trackPreconditionService = trackPreconditionService;
        this.chartPreconditionService = chartPreconditionService;
    }

    @Override
    @Transactional
    public Long addTrack(Long chartId, Long trackId, DataForToken tokenData) {
        Track track = this.trackPreconditionService.getIfExistsAndPublic(trackId);
        Chart chart = this.chartPreconditionService.getByIdIfExists(chartId);

        try {
            return this.trackByChartRepository.save(chartId, trackId);
        }
        catch (DataIntegrityViolationException e) {
            throw new ConflictException("Track (id=" + trackId + ") already exists in chart (id=" + chartId + ")");
        }
    }

    @Override
    @Transactional
    public void removeTrack(Long chartId, Long trackId, DataForToken tokenData) {
        Track track = this.trackPreconditionService.getByIdIfExists(trackId);
        Chart chart = this.chartPreconditionService.getByIdIfExists(chartId);
        this.trackByChartRepository.delete(chartId, trackId);
    }

}
