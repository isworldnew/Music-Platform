package ru.smirnov.musicplatform.service.abstraction.audience;

import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.authentication.LoginRequest;
import ru.smirnov.musicplatform.entity.audience.Account;
import ru.smirnov.musicplatform.entity.auxiliary.enums.AccountStatus;
import ru.smirnov.musicplatform.entity.auxiliary.enums.Role;

public interface AccountService {

    // создание аккаунта
    Account createAccount(LoginRequest dto, Role role, AccountStatus accountStatus);

    // обновление данных аккаунта (username + password)
    void updateAccount(LoginRequest dto, DataForToken tokenData);

    // обновление роли, наверное, в целом - не предусмотрено

    // обновление статуса - отдельно и только для SUPERADMIN
}
