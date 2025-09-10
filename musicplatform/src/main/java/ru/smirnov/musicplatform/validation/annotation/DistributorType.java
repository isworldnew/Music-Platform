package ru.smirnov.musicplatform.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.smirnov.musicplatform.validation.validator.DistributorTypeValidator;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = DistributorTypeValidator.class)
@Documented
public @interface DistributorType {

    String message() default "Invalid distributor type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
