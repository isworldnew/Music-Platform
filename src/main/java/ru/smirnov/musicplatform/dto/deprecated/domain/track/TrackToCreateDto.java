package ru.smirnov.musicplatform.dto.deprecated.domain.track;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.Set;

@Data
public class TrackToCreateDto {

    @NotNull @NotBlank @NotEmpty
    private String name;

    @NotNull @Positive
    private Long author;

    @NotNull // но необязательно пустой
    private Set<Long> coAuthors;

//    private MultipartFile cover;
//
//    @NotNull
//    private MultipartFile audio;

    @NotNull @NotBlank @NotEmpty
    private String genre;

}
