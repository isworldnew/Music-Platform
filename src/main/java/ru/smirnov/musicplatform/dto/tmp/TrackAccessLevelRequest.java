package ru.smirnov.musicplatform.dto.tmp;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.smirnov.musicplatform.validation.annotation.TrackAccessLevel;

@Data
public class TrackAccessLevelRequest {

    @NotBlank @TrackAccessLevel
    private String accessLevel;

}
