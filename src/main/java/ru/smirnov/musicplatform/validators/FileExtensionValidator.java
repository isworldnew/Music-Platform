package ru.smirnov.musicplatform.validators;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.smirnov.musicplatform.exception.InvalidFileExtensionException;
import ru.smirnov.musicplatform.validators.enums.ContentType;
import ru.smirnov.musicplatform.validators.interfaces.FileValidator;

@Component
public class FileExtensionValidator implements FileValidator {

    @Override
    public boolean validate(MultipartFile fileToCheck, ContentType contentType) {

        if (fileToCheck == null || fileToCheck.isEmpty()) return true;


        String fileName = fileToCheck.getOriginalFilename();

        int lastDotIndex = fileName.lastIndexOf(".");

        if (lastDotIndex == -1)
            throw new InvalidFileExtensionException("Unable to extract and check original file extension");

        String extension = fileName.substring(lastDotIndex).toLowerCase();


        if (!contentType.getAvailableTypes().contains(extension))
            throw new InvalidFileExtensionException("Invalid file extension: " + extension +". Available extensions: " + contentType.getAvailableTypes());

        return true;
    }
}
