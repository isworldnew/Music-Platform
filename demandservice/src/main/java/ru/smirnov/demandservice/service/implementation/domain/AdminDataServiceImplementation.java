package ru.smirnov.demandservice.service.implementation.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.demandservice.entity.domain.AdminData;
import ru.smirnov.demandservice.mapper.abstraction.AdminDataMapper;
import ru.smirnov.demandservice.repository.AdminDataRepository;
import ru.smirnov.demandservice.service.abstraction.domain.AdminDataService;
import ru.smirnov.demandservice.kafka.consumer.implementation.AdminDataMessage;
@Service
public class AdminDataServiceImplementation implements AdminDataService {

    private final AdminDataRepository adminDataRepository;
    private final AdminDataMapper adminDataMapper;

    @Autowired
    public AdminDataServiceImplementation(AdminDataRepository adminDataRepository, AdminDataMapper adminDataMapper) {
        this.adminDataRepository = adminDataRepository;
        this.adminDataMapper = adminDataMapper;
    }

    @Override
    @Transactional
    public Long saveAdminData(AdminDataMessage message) {
        AdminData adminData = this.adminDataMapper.adminDataMessageToAdminDataEntity(message);
        return this.adminDataRepository.save(adminData).getId();
    }
}
