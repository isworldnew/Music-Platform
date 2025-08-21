package ru.smirnov.musicplatform.repository.audience;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.repository.auxiliary.EntityRepository;

import java.util.Optional;

@Repository("UserRepository")
public interface UserRepository extends EntityRepository<User, Long> {

    @Query("SELECT user FROM User user WHERE user.data.phonenumber = :phonenumber")
    Optional<User> findByPhonenumber(String phonenumber);

    @Query("SELECT user FROM User user WHERE user.data.email = :email")
    Optional<User> findByEmail(String email);

}
