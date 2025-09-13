package ru.smirnov.demandservice.service.abstraction.domain;

import ru.smirnov.demandservice.kafka.consumer.implementation.AdminDataMessage;

public interface AdminDataService {
    Long saveAdminData(AdminDataMessage message);
}
