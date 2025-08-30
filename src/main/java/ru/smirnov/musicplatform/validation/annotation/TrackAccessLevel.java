package ru.smirnov.musicplatform.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.smirnov.musicplatform.validation.validator.TrackAccessLevelValidator;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = TrackAccessLevelValidator.class)
@Documented
public @interface TrackAccessLevel {

    String message() default "Invalid track's access level";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
