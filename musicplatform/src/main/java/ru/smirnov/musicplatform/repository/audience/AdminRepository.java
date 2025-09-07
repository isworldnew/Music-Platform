package ru.smirnov.musicplatform.repository.audience;

import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.audience.Admin;
import ru.smirnov.musicplatform.repository.auxiliary.EntityRepository;


@Repository("AdminRepository")
public interface AdminRepository extends EntityRepository<Admin, Long> {
}
