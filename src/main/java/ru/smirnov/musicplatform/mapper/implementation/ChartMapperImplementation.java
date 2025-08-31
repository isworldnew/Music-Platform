package ru.smirnov.musicplatform.mapper.implementation;

import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.tmp.MusicCollectionRequest;
import ru.smirnov.musicplatform.entity.audience.Admin;
import ru.smirnov.musicplatform.entity.domain.Chart;
import ru.smirnov.musicplatform.mapper.abstraction.ChartMapper;

@Component
public class ChartMapperImplementation implements ChartMapper {

    @Override
    public Chart musicCollectionRequestToChartEntity(MusicCollectionRequest dto, Admin admin) {
        Chart chart = new Chart();
        chart.setName(dto.getName());
        chart.setDescription(dto.getDescription());
        chart.setAdmin(admin);
        return chart;
    }
}
