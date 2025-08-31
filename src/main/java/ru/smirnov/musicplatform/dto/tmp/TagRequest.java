package ru.smirnov.musicplatform.dto.tmp;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TagRequest {

    @NotBlank
    private String name;
}
