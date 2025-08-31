package ru.smirnov.musicplatform.dto.deprecated.domain.track;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.smirnov.musicplatform.validation.annotation.TrackAccessLevel;

@Data
public class TrackAccessLevelUpdateDto {

    @NotNull @NotBlank @NotEmpty
    @TrackAccessLevel
    String trackAccessLevel;

}
