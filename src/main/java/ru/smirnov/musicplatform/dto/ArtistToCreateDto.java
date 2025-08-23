package ru.smirnov.musicplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Data
public class ArtistToCreateDto {

    @NotNull @NotBlank @NotEmpty
    private String name;

    private String description;

    private MultipartFile cover;

    // вот тут точно придётся писать кастмный валидатор: мапа либо вообще null, либо в ней (ключ, значение) никогда не null
    private Map<String, String> socialNetworks;

}
