package ru.smirnov.musicplatform.precondition.implementation.audience;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.entity.audience.Account;
import ru.smirnov.musicplatform.exception.NotFoundException;
import ru.smirnov.musicplatform.exception.UsernameOccupiedException;
import ru.smirnov.musicplatform.precondition.abstraction.audience.AccountPreconditionService;
import ru.smirnov.musicplatform.repository.audience.AccountRepository;

@Service
public class AccountPreconditionServiceImplementation implements AccountPreconditionService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountPreconditionServiceImplementation(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // найти по имени, сравнить id аккаунта

    // имя может прийти новое, а может - старое

    @Override
    public Account findById(Long accountId) {
        return this.accountRepository.findById(accountId).orElseThrow(
                () -> new NotFoundException("Account with id=" + accountId + " was not found")
         );
    }

    @Override
    public Account checkUsernameUniqueness(Long accountId, String requestName) {
        Account accountFoundById = this.findById(accountId);

        // значит, пользователь решил не обновлять username
        if (accountFoundById.getUsername().equals(requestName))
            return accountFoundById;

        // в противном же случае username - новый, и нужно глянуть, нет ли аккаунта с таким username

        Account accountFoundByName = this.accountRepository.findByUsername(requestName).orElse(null);

        if (accountFoundByName != null)
            throw new UsernameOccupiedException("Username '" + requestName + "' is occupied");

        return accountFoundById;
    }

}
