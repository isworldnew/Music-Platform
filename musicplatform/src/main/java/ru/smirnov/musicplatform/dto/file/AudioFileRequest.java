package ru.smirnov.musicplatform.dto.file;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import ru.smirnov.musicplatform.validation.annotation.Music;

@Data
public class AudioFileRequest {

    @Music
    private MultipartFile audioFile;
}
