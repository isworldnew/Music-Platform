package ru.smirnov.musicplatform.service.implementation.audience;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.audience.admin.AdminRequest;
import ru.smirnov.musicplatform.dto.audience.admin.AdminResponse;
import ru.smirnov.musicplatform.entity.audience.Admin;
import ru.smirnov.musicplatform.mapper.abstraction.AdminMapper;
import ru.smirnov.musicplatform.precondition.abstraction.audience.AdminPreconditionService;
import ru.smirnov.musicplatform.repository.audience.AdminRepository;
import ru.smirnov.musicplatform.service.abstraction.audience.AdminService;

@Service
public class AdminServiceImplementation implements AdminService {

    private final AdminRepository adminRepository;
    private final AdminPreconditionService adminPreconditionService;
    private final AdminMapper adminMapper;

    @Autowired
    public AdminServiceImplementation(
            AdminRepository adminRepository,
            AdminPreconditionService adminPreconditionService,
            AdminMapper adminMapper
    ) {
        this.adminRepository = adminRepository;
        this.adminPreconditionService = adminPreconditionService;
        this.adminMapper = adminMapper;
    }

    @Override
    public AdminResponse getAdminData(DataForToken tokenData) {
        Admin admin = this.adminPreconditionService.getByIdIfExists(tokenData.getEntityId());
        return this.adminMapper.adminEntityToAdminResponse(admin);
    }

    @Override
    public void updateAdminData(AdminRequest dto, DataForToken tokenData) {
        Admin admin = this.adminPreconditionService.getByIdIfExists(tokenData.getEntityId());

//        admin.set();

        this.adminRepository.save(admin);
    }
}
