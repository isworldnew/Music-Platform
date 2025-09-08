package ru.smirnov.dtoregistry.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.smirnov.dtoregistry.validation.validator.DistributionTypeValidator;

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

