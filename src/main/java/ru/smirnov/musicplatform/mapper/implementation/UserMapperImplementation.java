package ru.smirnov.musicplatform.mapper.implementation;

import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.audience.user.UserResponse;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.mapper.abstraction.UserMapper;

@Component
public class UserMapperImplementation implements UserMapper {

    @Override
    public UserResponse userEntityToUserResponse(User user) {
        UserResponse dto = new UserResponse();
        dto.setUserId(user.getId());
        dto.setAccountId(user.getAccount().getId());
        dto.setUsername(user.getAccount().getUsername());
        dto.setLastname(user.getData().getLastname());
        dto.setFirstname(user.getData().getFirstname());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setPhonenumber(user.getData().getPhonenumber());
        dto.setEmail(user.getData().getEmail());
        dto.setRegistrationDate(user.getRegistrationDate());
        return dto;
    }
}
