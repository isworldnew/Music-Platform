package ru.smirnov.musicplatform.dto.domain.album;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.smirnov.musicplatform.validation.annotation.MusicCollectionAccessLevel;

@Data
public class MusicCollectionAccessLevelUpdateDto {

    @NotNull @Positive
    private Long musicCollectionId;

    @NotNull @NotBlank @NotEmpty
    @MusicCollectionAccessLevel
    private String musicCollectionAccessLevel;

}
