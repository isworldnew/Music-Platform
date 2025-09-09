package ru.smirnov.dtoregistry.dto.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.smirnov.dtoregistry.validation.annotation.TrackAccessLevel;

// из:
// package ru.smirnov.musicplatform.dto.domain.track;

@Data @AllArgsConstructor @NoArgsConstructor
public class TrackAccessLevelRequest {

    @NotBlank @TrackAccessLevel
    private String accessLevel;

}