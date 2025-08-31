package ru.smirnov.musicplatform.dto.tmp;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CoArtistRequest {

    @NotNull @Positive
    private Long coArtistId;
}
