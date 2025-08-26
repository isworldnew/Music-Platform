package ru.smirnov.musicplatform.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import ru.smirnov.musicplatform.dto.validation.annotation.Image;

@Data
public class TestDto {

    @Image
    private MultipartFile image;

}
