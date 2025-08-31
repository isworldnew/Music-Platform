package ru.smirnov.musicplatform.dto.old.domain.album;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AlbumToCreateDto extends MusicCollectionToCreateDto {

    @NotNull @Positive
    private Long artistId;

}
