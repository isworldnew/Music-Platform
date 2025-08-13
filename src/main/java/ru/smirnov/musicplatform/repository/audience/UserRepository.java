package ru.smirnov.musicplatform.repository.audience;

import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.repository.auxiliary.EntityRepository;

@Repository("UserRepository")
public interface UserRepository extends EntityRepository<User, Long> {
}
