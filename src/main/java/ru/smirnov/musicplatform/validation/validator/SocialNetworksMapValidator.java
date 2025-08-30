package ru.smirnov.musicplatform.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.smirnov.musicplatform.validation.annotation.SocialNetworksMap;

import java.util.Map;

public class SocialNetworksMapValidator implements ConstraintValidator<SocialNetworksMap, Map<String, String>> {

    @Override
    public boolean isValid(Map<String, String> socialNetworks, ConstraintValidatorContext constraintValidatorContext) {

        // if (socialNetworks == null) return true;

        for (Map.Entry<String, String> entry : socialNetworks.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (key == null || value == null) return false;
            if (key.isEmpty() || value.isEmpty()) return false;
            if (key.isBlank() || value.isBlank()) return false;

        }

        return true;
    }

}
