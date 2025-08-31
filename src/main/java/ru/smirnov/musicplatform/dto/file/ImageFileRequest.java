package ru.smirnov.musicplatform.dto.file;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import ru.smirnov.musicplatform.validation.annotation.Image;

@Data
public class ImageFileRequest {

    @Image
    private MultipartFile imageFile;
}
