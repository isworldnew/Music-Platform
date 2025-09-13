package ru.smirnov.demandservice.mapper.abstraction;

import ru.smirnov.demandservice.entity.domain.AdminData;
import ru.smirnov.demandservice.kafka.consumer.implementation.AdminDataMessage;
public interface AdminDataMapper {

    AdminData adminDataMessageToAdminDataEntity(AdminDataMessage message);
}
