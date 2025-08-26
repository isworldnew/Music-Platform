package ru.smirnov.musicplatform.dto.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.smirnov.musicplatform.dto.validation.validator.ImageValidator;
import ru.smirnov.musicplatform.dto.validation.validator.TrackAccessLevelValidator;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = ImageValidator.class)
@Documented
public @interface Image {

    String message() default "Image's file is invalid: size excess / unacceptable or unreadable extension";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
