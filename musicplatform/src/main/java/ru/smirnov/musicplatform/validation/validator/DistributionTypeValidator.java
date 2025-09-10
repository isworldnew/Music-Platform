package ru.smirnov.musicplatform.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.smirnov.dtoregistry.entity.auxiliary.DistributorType;import ru.smirnov.musicplatform.validation.annotation.DistributionType;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class DistributionTypeValidator implements ConstraintValidator<DistributionType, String> {

    @Override
    public boolean isValid(String distributionType, ConstraintValidatorContext constraintValidatorContext) {
        Set<String> distributionTypes = Arrays.stream(DistributorType.values())
                .map(distributorType -> distributorType.name())
                .collect(Collectors.toSet());

        if (!distributionTypes.contains(distributionType)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "Distribution type valid values are: " + Arrays.toString(DistributorType.values())
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}
