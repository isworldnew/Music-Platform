package ru.smirnov.musicplatform.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.smirnov.musicplatform.validation.annotation.DistributionStatus;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class DistributionStatusValidator implements ConstraintValidator<DistributionStatus, String> {

    @Override
    public boolean isValid(String distributionStatus, ConstraintValidatorContext constraintValidatorContext) {
        Set<String> distributionStatuses = Arrays.stream(ru.smirnov.musicplatform.entity.auxiliary.enums.DistributionStatus.values())
                .map(status -> status.name())
                .collect(Collectors.toSet());

        if (!distributionStatuses.contains(distributionStatus)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "Distribution status valid values are: " + Arrays.toString(ru.smirnov.musicplatform.entity.auxiliary.enums.DistributionStatus.values())
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}
