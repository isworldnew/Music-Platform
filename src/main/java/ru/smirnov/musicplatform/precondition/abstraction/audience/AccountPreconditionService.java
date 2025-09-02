package ru.smirnov.musicplatform.precondition.abstraction.audience;

import ru.smirnov.musicplatform.entity.audience.Account;

public interface AccountPreconditionService {
    Account findById(Long accountId);

    Account checkUsernameUniqueness(Long accountId, String requestName);
}
