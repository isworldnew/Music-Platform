package ru.smirnov.musicplatform.mapper.abstraction;

import ru.smirnov.musicplatform.dto.tmp.MusicCollectionRequest;
import ru.smirnov.musicplatform.entity.audience.Admin;
import ru.smirnov.musicplatform.entity.domain.Chart;

public interface ChartMapper {

    Chart musicCollectionRequestToChartEntity(MusicCollectionRequest dto, Admin admin);
}
