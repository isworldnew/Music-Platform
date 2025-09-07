package ru.smirnov.musicplatform.mapper.abstraction;

import ru.smirnov.musicplatform.dto.audience.user.UserResponse;
import ru.smirnov.musicplatform.entity.audience.User;

public interface UserMapper {

    UserResponse userEntityToUserResponse(User user);

}
