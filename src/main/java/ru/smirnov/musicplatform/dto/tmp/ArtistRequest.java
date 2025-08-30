package ru.smirnov.musicplatform.dto.tmp;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArtistRequest {

    @NotBlank // оказывается: @NotBlank уже выполняет действия @NotNull и @NotEmpty
    private String name;

    private String description;

}
