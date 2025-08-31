package ru.smirnov.musicplatform.dto.old.domain.album;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import ru.smirnov.musicplatform.validation.annotation.Image;

import java.util.Set;

@Data
public class MusicCollectionToCreateDto {

    @NotNull @NotBlank @NotEmpty
    private String name;

    private String description;

    @Image
    private MultipartFile cover;

    private Set<Long> tracks;

}
