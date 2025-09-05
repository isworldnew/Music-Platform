package ru.smirnov.musicplatform.finder.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.finder.abstraction.ChartFinderService;
import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;
import ru.smirnov.musicplatform.projection.implementation.MusicCollectionShortcutProjectionImplementation;
import ru.smirnov.musicplatform.repository.domain.finder.ChartFinderRepository;

import java.util.List;

@Service
public class ChartFinderServiceImplementation implements ChartFinderService {

    private final ChartFinderRepository chartFinderRepository;

    @Autowired
    public ChartFinderServiceImplementation(ChartFinderRepository chartFinderRepository) {
        this.chartFinderRepository = chartFinderRepository;
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
}
