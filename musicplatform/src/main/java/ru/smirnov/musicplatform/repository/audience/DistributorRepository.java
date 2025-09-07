package ru.smirnov.musicplatform.repository.audience;

import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.audience.Distributor;
import ru.smirnov.musicplatform.repository.auxiliary.EntityRepository;

import java.util.Optional;

@Repository("DistributorRepository")
public interface DistributorRepository extends EntityRepository<Distributor, Long> {

    Optional<Distributor> findByName(String name);

}
