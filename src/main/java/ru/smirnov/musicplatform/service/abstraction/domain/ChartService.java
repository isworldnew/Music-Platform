package ru.smirnov.musicplatform.service.abstraction.domain;

import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionAccessLevelRequest;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionRequest;

public interface ChartService {

    Long createChart(MusicCollectionRequest dto, DataForToken tokenData);

    void updateChart(Long chartId, MusicCollectionRequest dto, DataForToken tokenData);

    void updateChartAccessLevel(Long chartId, MusicCollectionAccessLevelRequest dto, DataForToken tokenData);
}
