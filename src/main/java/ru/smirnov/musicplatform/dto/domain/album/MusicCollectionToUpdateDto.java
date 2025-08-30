package ru.smirnov.musicplatform.dto.domain.album;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import ru.smirnov.musicplatform.validation.annotation.Image;

@Data
public class MusicCollectionToUpdateDto {

    @NotNull @Positive
    private Long musicCollectionId;

    @NotNull @NotBlank @NotEmpty
    private String name;

    private String description;

    @Image
    private MultipartFile cover;

}
