package ru.smirnov.musicplatform.precondition.abstraction.audience;

import ru.smirnov.musicplatform.entity.audience.Distributor;

public interface DistributorPreconditionService {

    Distributor getByIdIfExists(Long distributorId);

//    Distributor nameUniquenessDuringUpdate(Long distributorId, String name);
}
