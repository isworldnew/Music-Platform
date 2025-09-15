package ru.smirnov.demandservice.mapper.abstraction;

import ru.smirnov.demandservice.entity.domain.AdminData;
import ru.smirnov.demandservice.message.AdminDataMessage;

public interface AdminDataMapper {

    AdminData adminDataMessageToAdminDataEntity(AdminDataMessage message);
}
