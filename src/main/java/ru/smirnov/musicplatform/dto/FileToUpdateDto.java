package ru.smirnov.musicplatform.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileToUpdateDto {

    private final MultipartFile file;

}
