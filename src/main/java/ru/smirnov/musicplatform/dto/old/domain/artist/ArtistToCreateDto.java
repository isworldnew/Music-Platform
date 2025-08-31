package ru.smirnov.musicplatform.dto.old.domain.artist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import ru.smirnov.musicplatform.validation.annotation.SocialNetworksMap;

import java.util.Map;

@Data
public class ArtistToCreateDto {

    @NotNull @NotBlank @NotEmpty
    private String name;

    private String description;

    private MultipartFile cover;

    @NotNull @SocialNetworksMap
    private Map<String, String> socialNetworks;

}
