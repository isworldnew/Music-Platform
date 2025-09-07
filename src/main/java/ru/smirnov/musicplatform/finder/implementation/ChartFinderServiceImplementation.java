package ru.smirnov.musicplatform.finder.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionResponse;
import ru.smirnov.musicplatform.entity.auxiliary.enums.Role;
import ru.smirnov.musicplatform.entity.domain.Chart;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.finder.abstraction.ChartFinderService;
import ru.smirnov.musicplatform.mapper.abstraction.ChartMapper;
import ru.smirnov.musicplatform.precondition.abstraction.domain.ChartPreconditionService;
import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;
import ru.smirnov.musicplatform.projection.abstraction.TrackShortcutProjection;
import ru.smirnov.musicplatform.projection.implementation.MusicCollectionShortcutProjectionImplementation;
import ru.smirnov.musicplatform.repository.domain.ChartRepository;
import ru.smirnov.musicplatform.repository.domain.finder.ChartFinderRepository;
import ru.smirnov.musicplatform.repository.domain.finder.TrackFinderRepository;

import java.util.List;

@Service
public class ChartFinderServiceImplementation implements ChartFinderService {

    private final ChartFinderRepository chartFinderRepository;
    private final ChartPreconditionService chartPreconditionService;
    private final ChartRepository chartRepository;
    private final TrackFinderRepository trackFinderRepository;
    private final ChartMapper chartMapper;

    @Autowired
    public ChartFinderServiceImplementation(
            ChartFinderRepository chartFinderRepository,
            ChartPreconditionService chartPreconditionService,
            ChartRepository chartRepository,
            TrackFinderRepository trackFinderRepository,
            ChartMapper chartMapper
    ) {
        this.chartFinderRepository = chartFinderRepository;
        this.chartPreconditionService = chartPreconditionService;
        this.chartRepository = chartRepository;
        this.trackFinderRepository = trackFinderRepository;
        this.chartMapper = chartMapper;
    }

    @Override
    public List<MusicCollectionShortcutProjection> searchCharts(String searchRequest, Long userId, boolean isSaved) {
        List<MusicCollectionShortcutProjection> charts = this.chartFinderRepository.searchCharts(searchRequest, userId, isSaved);

        for (MusicCollectionShortcutProjection chart : charts) {
            if (!chart.getAccessLevel().isAvailable()) {
                ((MusicCollectionShortcutProjectionImplementation) chart).setImageReference(null);
            }
        }

        return charts;
    }

    @Override
    public List<MusicCollectionShortcutProjection> searchChartsByAdmin(String searchRequest, Long adminId) {
        List<MusicCollectionShortcutProjection> charts = this.chartFinderRepository.searchChartsByAdmin(searchRequest, adminId);
        return charts;
    }

    @Override
    public List<MusicCollectionShortcutProjection> getSavedCharts(Long userId) {

        List<MusicCollectionShortcutProjection> charts = this.chartFinderRepository.getSavedCharts(userId);

        for (MusicCollectionShortcutProjection chart : charts) {
            if (!chart.getAccessLevel().isAvailable()) {
                ((MusicCollectionShortcutProjectionImplementation) chart).setImageReference(null);
            }
        }

        return charts;
    }

    @Override
    public List<MusicCollectionShortcutProjection> searchChartsGloballyAdmin(String searchRequest) {
        return this.chartFinderRepository.searchChartsGloballyAdmin(searchRequest);
    }

    @Override
    @Transactional
    public MusicCollectionResponse getChartById(Long chartId, DataForToken tokenData) {
        // для GUEST, USER и ADMIN

        Chart chart = this.chartPreconditionService.getByIdIfExists(chartId);
        List<TrackShortcutProjection> tracks;

        if (!tokenData.getRole().equals(Role.ADMIN.name())) {

            if (!chart.getAccessLevel().isAvailable())
                throw new ForbiddenException("Chart (id=" + chartId + ") is not PUBLIC");

            chart.setNumberOfPlays(chart.getNumberOfPlays() + 1);
            this.chartRepository.save(chart);

            tracks = this.trackFinderRepository.getTracksByChart(chartId, true);

            return this.chartMapper.chartEntityToMusicCollectionResponse(chart, tracks);
        }

        tracks = this.trackFinderRepository.getTracksByChart(chartId, false);
        return this.chartMapper.chartEntityToMusicCollectionResponse(chart, tracks);
    }

}
