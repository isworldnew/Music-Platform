package ru.smirnov.musicplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import ru.smirnov.musicplatform.dto.validation.annotation.SocialNetworksMap;

import java.util.Map;

@Data
public class ArtistToCreateDto {

    @NotNull @NotBlank @NotEmpty
    private String name;

    private String description;

    private MultipartFile cover;

    @SocialNetworksMap
    private Map<String, String> socialNetworks;

}
