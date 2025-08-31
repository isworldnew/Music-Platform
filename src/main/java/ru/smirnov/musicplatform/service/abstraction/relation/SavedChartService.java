package ru.smirnov.musicplatform.service.abstraction.relation;

import ru.smirnov.musicplatform.authentication.DataForToken;

public interface SavedChartService {

    Long addChart(Long chartId, DataForToken tokenData);

    void removeChart(Long chartId, DataForToken tokenData);
}
