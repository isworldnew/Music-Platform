package ru.smirnov.musicplatform.service.abstraction.relation;

import ru.smirnov.dtoregistry.dto.authentication.DataForToken;

public interface SavedChartService {

    Long addChart(Long chartId, DataForToken tokenData);

    void removeChart(Long chartId, DataForToken tokenData);
}
