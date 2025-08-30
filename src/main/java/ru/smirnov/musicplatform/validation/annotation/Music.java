package ru.smirnov.musicplatform.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.smirnov.musicplatform.validation.validator.ImageValidator;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = ImageValidator.class)
@Documented
public @interface Music {

    String message() default "Audiofile is invalid: size excess / unacceptable or unreadable extension";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
