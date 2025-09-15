package ru.smirnov.demandservice.mapper.implementation;

import org.springframework.stereotype.Component;
import ru.smirnov.demandservice.entity.domain.AdminData;
import ru.smirnov.demandservice.mapper.abstraction.AdminDataMapper;
import ru.smirnov.demandservice.kafka.consumer.implementation.AdminDataMessage;

@Component
public class AdminDataMapperImplementation implements AdminDataMapper {

    @Override
    public AdminData adminDataMessageToAdminDataEntity(AdminDataMessage message) {
        AdminData adminData = new AdminData();
        adminData.setAdminId(message.getId());
        adminData.setStatus(message.getAccountStatus());
        return adminData;
    }
}
