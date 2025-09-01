package ru.smirnov.musicplatform.service.abstraction.audience;

import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.audience.distributor.DistributorRequest;
import ru.smirnov.musicplatform.dto.audience.distributor.DistributorResponse;

public interface DistributorService {

    // просмотр бизнес-данных об аккаунте
    DistributorResponse getDistributorData(DataForToken tokenData);

    // обновление данных об аккаунте
    void updateDistributorData(DistributorRequest dto, DataForToken tokenData);
}
