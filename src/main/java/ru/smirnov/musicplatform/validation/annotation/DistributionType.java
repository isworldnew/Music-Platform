package ru.smirnov.musicplatform.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.smirnov.musicplatform.validation.validator.DistributionTypeValidator;
import ru.smirnov.musicplatform.validation.validator.ImageValidator;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = DistributionTypeValidator.class)
@Documented
public @interface DistributionType {

    String message() default "Invalid distribution type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
