package ru.smirnov.musicplatform.dto.domain.album;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.Set;

@Data
public class MusicCollectionTracksDto {

    @NotNull @Positive
    private Long musicCollectionId;

    @NotNull
    private Set<Long> tracks;

}
