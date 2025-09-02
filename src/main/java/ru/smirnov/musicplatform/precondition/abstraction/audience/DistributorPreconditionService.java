package ru.smirnov.musicplatform.precondition.abstraction.audience;

import ru.smirnov.musicplatform.entity.audience.Distributor;

public interface DistributorPreconditionService {

    Distributor findByIdIfExists(Long distributorId);

}
