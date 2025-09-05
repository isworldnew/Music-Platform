package ru.smirnov.musicplatform.finder.abstraction;

import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;

import java.util.List;

public interface ChartFinderService {

    List<MusicCollectionShortcutProjection> searchCharts(String searchRequest, Long userId, boolean isSaved);

}
