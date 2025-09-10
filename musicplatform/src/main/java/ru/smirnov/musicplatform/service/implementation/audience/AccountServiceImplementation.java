package ru.smirnov.musicplatform.service.implementation.audience;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.dtoregistry.dto.authentication.LoginRequest;
import ru.smirnov.musicplatform.entity.audience.Account;
import ru.smirnov.dtoregistry.entity.auxiliary.AccountStatus;import ru.smirnov.musicplatform.entity.auxiliary.enums.Role;
import ru.smirnov.musicplatform.exception.NonUniqueAccountPerEntity;
import ru.smirnov.musicplatform.exception.UsernameOccupiedException;
import ru.smirnov.musicplatform.precondition.abstraction.audience.AccountPreconditionService;
import ru.smirnov.musicplatform.repository.audience.AccountRepository;
import ru.smirnov.musicplatform.repository.auxiliary.EntityRepository;
import ru.smirnov.musicplatform.service.abstraction.audience.AccountService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class AccountServiceImplementation implements UserDetailsService, AccountService {

    private final AccountRepository accountRepository;
    private final Map<String, EntityRepository<?, Long>> entityRepositories;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final AccountPreconditionService accountPreconditionService;

    @Autowired
    public AccountServiceImplementation(
            AccountRepository accountRepository,
            Map<String, EntityRepository<?, Long>> entityRepositories,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            AccountPreconditionService accountPreconditionService
    ) {
        this.accountRepository = accountRepository;
        this.entityRepositories = entityRepositories;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.accountPreconditionService = accountPreconditionService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DisabledException {

        Account account = this.accountRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("No Account with such Username")
        );

//        if (!account.getStatus().isEnabled()) throw new DisabledException("Account is disabled");

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + account.getRole().name())
        );


        /*
        я пока нигде не использую роль SUPERADMIN, но тем не менее, эта та роль,
        для которой не определена сущность и таблица в БД
        это значит, что в объекте класса DataForToken поле entityId будет null

        поэтому базово сделаю это поле со значением null
        при хорошем раскладе для неё найдётся значение
        */
        Long entityId = null;


        /*
        нужно пройти по ключам Map-ы и попробовать найти id связанной с аккаунтом бизнес-сущности
        если все null - подразумеваем, что это SUPERADMIN (аккаунт без бизнес-данных), потому что
        случаев, кодга сущность с бизнес-данными отдельно от аккаунта будем стараться избегать

        а вот если нам попалась более, чем одна сущность, привязанная к аккаунту - это недопустимый
        случай и серьёзная проблема, нужно кидать исключение
        хотя я и установил связь между аккаунтами и сущностями как 1:1, но пусть будет
        */
        int counter = 0;
        for (String entityRepository : this.entityRepositories.keySet()) {
            Long id = this.entityRepositories.get(entityRepository).findEntityIdByAccountId(account.getId()).orElse(null);
            if (id != null) {
                entityId = id;
                counter += 1;
            }
        }

        if (counter > 1) throw new NonUniqueAccountPerEntity("There is an account connected with more than one business-entity");


        return DataForToken.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .enabled(account.getStatus().isEnabled())
                .authorities(authorities)
                .accountId(account.getId())
                // это вспомогательное доп. поле для меня, а не SimpleGrantedAuthority, поэтому не буду его начинать с ROLE_
                .role(account.getRole().name())
                .entityId(entityId)
                .build();

    }



    // обычные методы для работы с данной сущностью (не относящиеся к UserDetailsService)

    // подразумеваю, что данный метод не будет вызываться самостоятельно (только при создании сущности с бизнес-данными),
    // поэтому уровень изоляции оставляю по умолчанию
    @Override
    @Transactional
    public Account createAccount(LoginRequest dto, Role role, AccountStatus accountStatus, boolean passwordAlreadyHashed) {

        if (this.accountRepository.findByUsername(dto.getUsername()).isPresent())
            throw new UsernameOccupiedException("Such username is already in use by other account");

        Account newAccount = new Account();
        newAccount.setUsername(dto.getUsername());
        if (passwordAlreadyHashed)
            newAccount.setPassword(dto.getPassword());
        else
            newAccount.setPassword(this.bCryptPasswordEncoder.encode(dto.getPassword()));
        newAccount.setRole(role);
        newAccount.setStatus(accountStatus);

        return this.accountRepository.save(newAccount);

    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void updateAccount(LoginRequest dto, DataForToken tokenData) {
        Account account = this.accountPreconditionService.checkUsernameUniqueness(tokenData.getAccountId(), dto.getUsername());

        account.setUsername(dto.getUsername());
        account.setPassword(this.bCryptPasswordEncoder.encode(dto.getPassword()));

        this.accountRepository.save(account);
    }

}
