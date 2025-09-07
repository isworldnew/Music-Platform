package ru.smirnov.musicplatform.dto.domain.musiccollection;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.smirnov.musicplatform.validation.annotation.MusicCollectionAccessLevel;

@Data
public class MusicCollectionAccessLevelRequest {

    @NotBlank @MusicCollectionAccessLevel
    private String accessLevel;

}
