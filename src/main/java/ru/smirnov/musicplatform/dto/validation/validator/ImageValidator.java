package ru.smirnov.musicplatform.dto.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;
import ru.smirnov.musicplatform.dto.validation.annotation.Image;
import ru.smirnov.musicplatform.validators.old.enums.ContentType;

public class ImageValidator implements ConstraintValidator<Image, MultipartFile> {
    @Override
    public boolean isValid(MultipartFile fileToValidate, ConstraintValidatorContext context) {
        return (
                FileValidator.validateSize(fileToValidate, ContentType.IMAGE, context)
                        &&
                FileValidator.validateExtension(fileToValidate, ContentType.IMAGE, context)
        );
    }
}
