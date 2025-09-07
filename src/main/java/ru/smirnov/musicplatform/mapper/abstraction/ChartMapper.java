package ru.smirnov.musicplatform.mapper.abstraction;

import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionRequest;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionResponse;
import ru.smirnov.musicplatform.entity.audience.Admin;
import ru.smirnov.musicplatform.entity.domain.Chart;
import ru.smirnov.musicplatform.projection.abstraction.TrackShortcutProjection;

import java.util.List;

public interface ChartMapper {

    Chart musicCollectionRequestToChartEntity(MusicCollectionRequest dto, Admin admin);

    MusicCollectionResponse chartEntityToMusicCollectionResponse(Chart chart, List<TrackShortcutProjection> tracks);
}
