package ru.smirnov.musicplatform.dto.domain.album;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import ru.smirnov.musicplatform.dto.validation.annotation.Image;

import java.util.Set;

@Data
public class MusicCollectionToCreateDto {

    @NotNull @Positive
    private Long creatorId; // id того, кто может быть автором какой-либо коллекции: [user: playlist; admin: chart; artist: album]

    @NotNull @NotBlank @NotEmpty
    private String name;

    private String description;

    @Image
    private MultipartFile cover;

    private Set<Long> tracks;

}
