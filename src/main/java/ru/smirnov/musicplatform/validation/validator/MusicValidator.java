package ru.smirnov.musicplatform.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;
import ru.smirnov.musicplatform.validation.ContentType;
import ru.smirnov.musicplatform.validation.annotation.Music;

public class MusicValidator implements ConstraintValidator<Music, MultipartFile> {
    @Override
    public boolean isValid(MultipartFile fileToValidate, ConstraintValidatorContext context) {
        return (
                FileValidator.validateSize(fileToValidate, ContentType.AUDIO, context)
                        &&
                FileValidator.validateExtension(fileToValidate, ContentType.AUDIO, context)
        );
    }
}