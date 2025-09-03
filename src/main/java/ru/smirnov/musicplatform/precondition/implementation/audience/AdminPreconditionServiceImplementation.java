package ru.smirnov.musicplatform.precondition.implementation.audience;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.precondition.abstraction.audience.AdminPreconditionService;
import ru.smirnov.musicplatform.repository.audience.AdminRepository;

@Service
public class AdminPreconditionServiceImplementation implements AdminPreconditionService {

    private final AdminRepository adminRepository;

    @Autowired
    public AdminPreconditionServiceImplementation(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }


}
