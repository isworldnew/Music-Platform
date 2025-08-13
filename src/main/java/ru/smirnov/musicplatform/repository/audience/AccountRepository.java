package ru.smirnov.musicplatform.repository.audience;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.audience.Account;

import java.util.Optional;

@Repository("AccountRepository")
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT account from Account account WHERE account.username = :username")
    Optional<Account> findByUsername(String username);

}
