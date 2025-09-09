package ru.smirnov.musicplatform.repository.audience;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.audience.Admin;
import ru.smirnov.musicplatform.repository.auxiliary.EntityRepository;

import java.util.List;
import java.util.Optional;


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

    @Query("SELECT admin FROM Admin admin WHERE admin.data.phonenumber = :phonenumber")
    Optional<Admin> findByPhonenumber(String phonenumber);

    @Query("SELECT admin FROM Admin admin WHERE admin.data.email = :email")
    Optional<Admin> findByEmail(String email);
}
