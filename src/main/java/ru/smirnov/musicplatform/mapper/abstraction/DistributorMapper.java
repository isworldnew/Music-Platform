package ru.smirnov.musicplatform.mapper.abstraction;

import ru.smirnov.musicplatform.dto.audience.distributor.DistributorResponse;
import ru.smirnov.musicplatform.dto.audience.distributor.DistributorShortcutResponse;
import ru.smirnov.musicplatform.dto.audience.distributor.ExtendedDistributorResponse;
import ru.smirnov.musicplatform.entity.audience.Distributor;

public interface DistributorMapper {

    DistributorResponse distributorEntityToDistributorResponse(Distributor distributor);

    DistributorShortcutResponse distributorEntityToDistributorShortcutResponse(Distributor distributor);

    ExtendedDistributorResponse distributorEntityToExtendedDistributorResponse(Distributor distributor);
}
