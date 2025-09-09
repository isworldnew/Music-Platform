package ru.smirnov.musicplatform.precondition.implementation.audience;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.entity.audience.Admin;
import ru.smirnov.musicplatform.exception.EmailOccupiedException;
import ru.smirnov.musicplatform.exception.PhonenumberOccupiedException;
import ru.smirnov.musicplatform.precondition.abstraction.audience.AdminPreconditionService;
import ru.smirnov.musicplatform.repository.audience.AdminRepository;

@Service
public class AdminPreconditionServiceImplementation implements AdminPreconditionService {

    private final AdminRepository adminRepository;

    @Autowired
    public AdminPreconditionServiceImplementation(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public Admin getByIdIfExists(Long adminId) {
        return null;
    }

    @Override
    public void checkExistenceByPhonenumberOrEmail(String phonenumber, String email) {
        if (this.adminRepository.findByPhonenumber(phonenumber).isPresent())
            throw new PhonenumberOccupiedException("Admin with phonenumber='" + phonenumber + "' already exists");

        if (this.adminRepository.findByEmail(email).isPresent())
            throw new EmailOccupiedException("Admin with email='" + email + "' already exists");
    }

}
