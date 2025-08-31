package ru.smirnov.musicplatform.dto.deprecated.domain.artist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ArtistToUpdateDto {

    @NotNull @NotBlank @NotEmpty
    public String name;

    public String description;

}
