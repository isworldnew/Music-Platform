package ru.smirnov.musicplatform.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.smirnov.musicplatform.validation.validator.SocialNetworksMapValidator;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = SocialNetworksMapValidator.class)
@Documented
public @interface SocialNetworksMap {

    String message() default "Map of Social Networks should have filled keys and values (not null, not empty, not blank) or should be fully empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
