package ru.smirnov.musicplatform.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.smirnov.musicplatform.validation.validator.DistributionStatusValidator;
import ru.smirnov.musicplatform.validation.validator.DistributorTypeValidator;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = DistributionStatusValidator.class)
@Documented
public @interface DistributionStatus {

    String message() default "Invalid distribution status";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
