package ru.smirnov.musicplatform.dto.domain.musiccollection;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class MusicCollectionTracksUpdateRequest {

    @NotNull
    Set<Long> newTrackSet;

}
