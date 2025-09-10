package ru.smirnov.dtoregistry.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.smirnov.dtoregistry.validation.validator.DemandStatusValidator;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = DemandStatusValidator.class)
@Documented
public @interface DemandStatus {

    String message() default "Invalid demand status";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
