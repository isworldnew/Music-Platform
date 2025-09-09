package ru.smirnov.musicplatform.service.abstraction.audience;

import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.audience.user.UserDataUpdateRequest;
import ru.smirnov.musicplatform.dto.audience.user.UserRegistrationRequest;
import ru.smirnov.musicplatform.dto.audience.user.UserResponse;
import ru.smirnov.musicplatform.entity.audience.User;

public interface UserService {

    User userRegistration(UserRegistrationRequest dto);

    // просмотр бизнес-данных об аккаунте
    UserResponse getUserData(DataForToken tokenData);

    // обновление данных об аккаунте
    void updateUserData(UserDataUpdateRequest dto, DataForToken tokenData);
}
