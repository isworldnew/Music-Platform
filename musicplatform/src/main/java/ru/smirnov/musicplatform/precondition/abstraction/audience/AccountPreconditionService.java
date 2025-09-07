package ru.smirnov.musicplatform.precondition.abstraction.audience;

import ru.smirnov.musicplatform.entity.audience.Account;

public interface AccountPreconditionService {
    Account findByIdIfExists(Long accountId);

    Account checkUsernameUniqueness(Long accountId, String requestName);
}
