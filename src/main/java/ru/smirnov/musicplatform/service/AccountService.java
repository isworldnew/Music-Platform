package ru.smirnov.musicplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.entity.audience.Account;
import ru.smirnov.musicplatform.exception.NonUniqueAccountPerEntity;
import ru.smirnov.musicplatform.repository.audience.AccountRepository;
import ru.smirnov.musicplatform.repository.auxiliary.EntityRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final Map<String, EntityRepository<?, Long>> entityRepositories;

    @Autowired
    public AccountService(
            AccountRepository accountRepository,
            Map<String, EntityRepository<?, Long>> entityRepositories
    ) {
        this.accountRepository = accountRepository;
        this.entityRepositories = entityRepositories;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DisabledException {

        Account account = this.accountRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("No Account with such Username")
        );

        if (!account.getStatus().isEnabled()) throw new DisabledException("Account is disabled");

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + account.getRole().name())
        );

        // я пока нигде не использую роль SUPERADMIN, но тем не менее, эта та роль,
        // для которой не определена сущность и таблица в БД
        // это значит, что в объекте класса DataForToken поле entityId будет null

        // поэтому базово сделаю это поле со значением null
        // при хорошем раскладе для неё найдётся значение
        Long entityId = null;


        // нужно пройти по ключам Map-ы и попробовать найти id связанной с аккаунтом бизнес-сущности
        // если все null - подразумеваем, что это SUPERADMIN (аккаунт без бизнес-данных), потому что
        // случаев, кодга сущность с бизнес-данными отдельно от аккаунта будем стараться избегать

        // а вот если нам попалась более, чем одна сущность, привязанная к аккаунту - это недопустимый
        // случай и серьёзная проблема, нужно кидать исключение
        // хотя я и установил связь между аккаунтами и сущностями как 1:1, но пусть будет

        int counter = 0;
        for (String entityRepository : this.entityRepositories.keySet()) {
            Long id = this.entityRepositories.get(entityRepository).findEntityIdByAccountId(account.getId()).orElse(null);
            if (id != null) {
                entityId = id;
                counter += 1;
            }
        }

        if (counter > 1) throw new NonUniqueAccountPerEntity("There is an account connected with more than one business-entity");


        DataForToken dataForToken = DataForToken.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .enabled(account.getStatus().isEnabled())
                .authorities(authorities)
                .accountId(account.getId())
                .role("ROLE_" + account.getRole().name())
                .entityId(entityId)
                .build();

        return dataForToken;
    }
}
