package ru.smirnov.demandservice.service.abstraction.domain;

import ru.smirnov.dtoregistry.message.AdminDataMessage;

public interface AdminDataService {
    Long saveAdminData(AdminDataMessage message);
}
