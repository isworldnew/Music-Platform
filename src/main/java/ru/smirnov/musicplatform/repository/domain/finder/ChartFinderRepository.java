package ru.smirnov.musicplatform.repository.domain.finder;

import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;

import java.util.List;

public interface ChartFinderRepository {

    List<MusicCollectionShortcutProjection> searchCharts(String searchRequest, Long userId, boolean savedOnly);

}
