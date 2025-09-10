package ru.smirnov.musicplatform.service.abstraction.audience;

import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.dtoregistry.dto.authentication.LoginRequest;import ru.smirnov.musicplatform.entity.audience.Account;
import ru.smirnov.dtoregistry.entity.auxiliary.AccountStatus;import ru.smirnov.musicplatform.entity.auxiliary.enums.Role;

public interface AccountService {

    // создание аккаунта
    Account createAccount(LoginRequest dto, Role role, AccountStatus accountStatus, boolean passwordAlreadyHashed);

    // обновление данных аккаунта (username + password)
    void updateAccount(LoginRequest dto, DataForToken tokenData);

    // обновление роли, наверное, в целом - не предусмотрено

    // обновление статуса - отдельно и только для SUPERADMIN
}
