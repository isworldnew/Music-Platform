package ru.smirnov.musicplatform.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.smirnov.musicplatform.validation.annotation.DistributorType;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class DistributorTypeValidator implements ConstraintValidator<DistributorType, String> {

    @Override
    public boolean isValid(String distributionType, ConstraintValidatorContext constraintValidatorContext) {
        Set<String> distributorTypes = Arrays.stream(ru.smirnov.dtoregistry.entity.auxiliary.DistributorType.values())
                .map(distributorType -> distributorType.name())
                .collect(Collectors.toSet());

        if (!distributorTypes.contains(distributionType)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "Distributor type valid values are: " + Arrays.toString(ru.smirnov.dtoregistry.entity.auxiliary.DistributorType.values())
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}
