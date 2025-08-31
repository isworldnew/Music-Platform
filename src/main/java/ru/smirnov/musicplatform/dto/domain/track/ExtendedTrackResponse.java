package ru.smirnov.musicplatform.dto.domain.track;

import lombok.Data;
import ru.smirnov.musicplatform.dto.domain.tag.TagResponse;

import java.util.List;

@Data
public class ExtendedTrackResponse extends TrackResponse {

    private List<TagResponse> tags;
}
