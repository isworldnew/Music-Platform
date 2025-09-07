package ru.smirnov.musicplatform.mapper.implementation;

import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionOwnerResponse;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionRequest;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionResponse;
import ru.smirnov.musicplatform.entity.audience.Admin;
import ru.smirnov.musicplatform.entity.domain.Chart;
import ru.smirnov.musicplatform.mapper.abstraction.ChartMapper;
import ru.smirnov.musicplatform.projection.abstraction.TrackShortcutProjection;

import java.util.List;

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

    @Override
    public MusicCollectionResponse chartEntityToMusicCollectionResponse(Chart chart, List<TrackShortcutProjection> tracks) {
        MusicCollectionResponse dto = new MusicCollectionResponse();
        dto.setId(chart.getId());
        dto.setName(chart.getName());
        dto.setDescription(chart.getDescription());
        dto.setNumberOfPlays(chart.getNumberOfPlays());
        dto.setImageReference(chart.getImageReference());
        dto.setUploadDateTime(chart.getUploadDateTime());
        dto.setAccessLevel(chart.getAccessLevel().name());
        dto.setOwner(new MusicCollectionOwnerResponse(chart.getAdmin().getId(), chart.getAdmin().getAccount().getUsername() + " [PLATFORM]"));
        dto.setIsSaved(null);
        dto.setTracks(tracks);
        return dto;
    }
}
