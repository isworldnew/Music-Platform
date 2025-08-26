package ru.smirnov.musicplatform.dto.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.smirnov.musicplatform.dto.validation.validator.TrackAccessLevelValidator;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = TrackAccessLevelValidator.class)
@Documented
public @interface Image {

    String message() default ;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
