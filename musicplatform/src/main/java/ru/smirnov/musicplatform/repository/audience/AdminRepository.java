package ru.smirnov.musicplatform.repository.audience;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.audience.Admin;
import ru.smirnov.musicplatform.repository.auxiliary.EntityRepository;

import java.util.List;


@Repository("AdminRepository")
public interface AdminRepository extends EntityRepository<Admin, Long> {

    @Query(
            value = """
                    SELECT
                        admins.id
                    FROM
                        admins
                    INNER JOIN accounts
                    ON admins.account_id = accounts.id
                    WHERE accounts.status = 'ENABLED'
                    """,
            nativeQuery = true
    )
    List<Long> findAllEnabledAdmins();

}
