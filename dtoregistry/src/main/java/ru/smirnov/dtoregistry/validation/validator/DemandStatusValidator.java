package ru.smirnov.dtoregistry.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.smirnov.dtoregistry.entity.auxiliary.DistributorType;
import ru.smirnov.dtoregistry.validation.annotation.DemandStatus;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class DemandStatusValidator implements ConstraintValidator<DemandStatus, String> {

    @Override
    public boolean isValid(String demandStatus, ConstraintValidatorContext constraintValidatorContext) {
        Set<String> demandStatuses = Arrays.stream(ru.smirnov.dtoregistry.entity.auxiliary.DemandStatus.values())
                .map(status -> status.name())
                .collect(Collectors.toSet());

        if (!demandStatuses.contains(demandStatus)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "Demand status valid values are: " + Arrays.toString(
                            ru.smirnov.dtoregistry.entity.auxiliary.DemandStatus.values()
                    )
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}
