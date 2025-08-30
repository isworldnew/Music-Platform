package ru.smirnov.musicplatform.dto.validation.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;
import ru.smirnov.musicplatform.validators.old.enums.ContentType;

public class FileValidator {

    public static boolean validateExtension(MultipartFile fileToValidate, ContentType contentType, ConstraintValidatorContext context) {

        // файл может просто не прийти
        if (fileToValidate == null || fileToValidate.isEmpty()) return true;

        String fileName = fileToValidate.getOriginalFilename();

        int lastDotIndex = fileName.lastIndexOf(".");

        if (lastDotIndex == -1) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Unable to extract and check original file extension").addConstraintViolation();
            return false;
        };

        String extension = fileName.substring(lastDotIndex).toLowerCase();

        if (!contentType.getAvailableTypes().contains(extension)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Invalid file extension: " + extension +". Available extensions: " + contentType.getAvailableTypes()
            ).addConstraintViolation();
            return false;
        }

        return true;
    }

    public static boolean validateSize(MultipartFile fileToValidate, ContentType contentType, ConstraintValidatorContext context) {

        if (fileToValidate == null || fileToValidate.isEmpty()) return true;

        if (fileToValidate.getSize() > contentType.getMaxSizeBytes()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    contentType.name() + " max size is ~" + (contentType.getMaxSizeBytes() / 1024) + " kilobytes, your is ~" + (fileToValidate.getSize() / 1024) + " kilobytes"
            ).addConstraintViolation();
            return false;
        }

        return true;
    }

}
