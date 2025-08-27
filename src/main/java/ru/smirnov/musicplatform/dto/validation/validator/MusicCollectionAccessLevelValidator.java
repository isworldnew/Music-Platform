package ru.smirnov.musicplatform.dto.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.smirnov.musicplatform.dto.validation.annotation.MusicCollectionAccessLevel;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class MusicCollectionAccessLevelValidator implements ConstraintValidator<MusicCollectionAccessLevel, String> {
    
    @Override
    public boolean isValid(String accessLevel, ConstraintValidatorContext constraintValidatorContext) {

        Set<String> collectionStatuses = Arrays.stream(
                ru.smirnov.musicplatform.entity.auxiliary.enums.MusicCollectionAccessLevel.values()).map(Enum::name).collect(Collectors.toSet()
        );

        if (!collectionStatuses.contains(accessLevel)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "Music collection's access level valid values are: " + Arrays.toString(
                            ru.smirnov.musicplatform.entity.auxiliary.enums.MusicCollectionAccessLevel.values()
                    )
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
