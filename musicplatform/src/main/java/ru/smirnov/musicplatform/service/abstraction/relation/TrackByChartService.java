package ru.smirnov.musicplatform.service.abstraction.relation;


import ru.smirnov.dtoregistry.dto.authentication.DataForToken;

public interface TrackByChartService {

    Long addTrack(Long chartId, Long trackId, DataForToken tokenData);

    void removeTrack(Long chartId, Long trackId, DataForToken tokenData);
}
