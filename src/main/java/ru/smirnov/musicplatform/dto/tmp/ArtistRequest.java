package ru.smirnov.musicplatform.dto.tmp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.smirnov.musicplatform.dto.validation.annotation.SocialNetworksMap;

import java.util.Map;

@Data
public class ArtistRequest {

    @NotBlank // оказывается: @NotBlank уже выполняет действия @NotNull и @NotEmpty
    private String name;

    private String description;

}
