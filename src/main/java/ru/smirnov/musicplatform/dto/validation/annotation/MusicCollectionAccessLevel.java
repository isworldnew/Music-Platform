package ru.smirnov.musicplatform.dto.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.smirnov.musicplatform.dto.validation.validator.MusicCollectionAccessLevelValidator;
import ru.smirnov.musicplatform.dto.validation.validator.TrackAccessLevelValidator;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = MusicCollectionAccessLevelValidator.class)
@Documented
public @interface MusicCollectionAccessLevel {

    String message() default "Invalid track's access level";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
