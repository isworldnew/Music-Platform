package ru.smirnov.musicplatform.dto.domain.track;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TrackRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String genre;

}
