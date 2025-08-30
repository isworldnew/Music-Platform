package ru.smirnov.musicplatform.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.smirnov.musicplatform.validation.validator.MusicCollectionAccessLevelValidator;

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
