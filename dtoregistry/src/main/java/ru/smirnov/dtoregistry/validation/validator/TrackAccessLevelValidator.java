package ru.smirnov.dtoregistry.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.smirnov.dtoregistry.entity.auxiliary.TrackStatus;
import ru.smirnov.dtoregistry.validation.annotation.TrackAccessLevel;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

// из:
// package ru.smirnov.musicplatform.validation.validator;

public class TrackAccessLevelValidator implements ConstraintValidator<TrackAccessLevel, String> {

    @Override
    public boolean isValid(String accessLevel, ConstraintValidatorContext constraintValidatorContext) {

        Set<String> trackStatuses = Arrays.stream(TrackStatus.values()).map(Enum::name).collect(Collectors.toSet());

        if (!trackStatuses.contains(accessLevel)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "Track's access level valid values are: " + Arrays.toString(TrackStatus.values())
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
