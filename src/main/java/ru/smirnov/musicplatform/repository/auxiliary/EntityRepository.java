package ru.smirnov.musicplatform.repository.auxiliary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface EntityRepository<T, ID> extends JpaRepository<T, ID> {

    @Query("SELECT entity.id FROM #{#entityName} entity WHERE entity.account.id = :accountId")
    Optional<ID> findEntityIdByAccountId(ID accountId);

}