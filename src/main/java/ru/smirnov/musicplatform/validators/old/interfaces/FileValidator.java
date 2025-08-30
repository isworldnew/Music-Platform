package ru.smirnov.musicplatform.validators.old.interfaces;

import org.springframework.web.multipart.MultipartFile;
import ru.smirnov.musicplatform.validators.old.enums.ContentType;

@FunctionalInterface
public interface FileValidator {

    boolean validate(MultipartFile fileToCheck, ContentType contentType);

}
