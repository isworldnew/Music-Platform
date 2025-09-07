package ru.smirnov.musicplatform.dto.domain.musiccollection;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MusicCollectionRequest {

    @NotBlank
    private String name;

    private String description;
}
