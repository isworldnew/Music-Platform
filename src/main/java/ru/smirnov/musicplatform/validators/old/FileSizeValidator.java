package ru.smirnov.musicplatform.validators.old;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.smirnov.musicplatform.exception.FileSizeExcessException;
import ru.smirnov.musicplatform.validators.old.enums.ContentType;
import ru.smirnov.musicplatform.validators.old.interfaces.FileValidator;

@Component
public class FileSizeValidator implements FileValidator {

    @Override
    public boolean validate(MultipartFile fileToCheck, ContentType contentType) {

        if (fileToCheck == null || fileToCheck.isEmpty()) return true;

        if (fileToCheck.getSize() > contentType.getMaxSizeBytes())
            throw new FileSizeExcessException(
                    contentType.name() + " max size is ~" + (contentType.getMaxSizeBytes() / 1024) + " kilobytes, your is ~" + (fileToCheck.getSize() / 1024) + " kilobytes"
            );

        return true;
    }

}
