package ru.smirnov.musicplatform.validators.interfaces;

import org.springframework.web.multipart.MultipartFile;
import ru.smirnov.musicplatform.validators.enums.ContentType;

@FunctionalInterface
public interface FileValidator {

    boolean validate(MultipartFile fileToCheck, ContentType contentType);

}
