package ru.smirnov.musicplatform.dto.relation;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArtistSocialNetworkRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String reference;
}
