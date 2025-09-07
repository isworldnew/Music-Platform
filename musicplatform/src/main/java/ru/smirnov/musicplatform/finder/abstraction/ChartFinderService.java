package ru.smirnov.musicplatform.finder.abstraction;

import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionResponse;
import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;

import java.util.List;

public interface ChartFinderService {

    List<MusicCollectionShortcutProjection> searchCharts(String searchRequest, Long userId, boolean isSaved);

    List<MusicCollectionShortcutProjection> searchChartsByAdmin(String searchRequest, Long adminId);

    List<MusicCollectionShortcutProjection> getSavedCharts(Long userId);

    List<MusicCollectionShortcutProjection> searchChartsGloballyAdmin(String searchRequest);

    MusicCollectionResponse getChartById(Long chartId, DataForToken tokenData);
}
