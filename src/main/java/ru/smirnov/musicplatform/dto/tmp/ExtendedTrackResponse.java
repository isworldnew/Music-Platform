package ru.smirnov.musicplatform.dto.tmp;

import lombok.Data;

import java.util.List;

@Data
public class ExtendedTrackResponse extends TrackResponse {

    private List<TagResponse> tags;
}
