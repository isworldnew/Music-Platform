package ru.smirnov.musicplatform.mapper.implementation;

import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.audience.admin.AdminRegistrationRequest;
import ru.smirnov.musicplatform.dto.audience.admin.AdminResponse;
import ru.smirnov.musicplatform.entity.audience.Account;
import ru.smirnov.musicplatform.entity.audience.Admin;
import ru.smirnov.musicplatform.entity.auxiliary.embedding.CommonPersonData;
import ru.smirnov.musicplatform.mapper.abstraction.AdminMapper;

@Component
public class AdminMapperImplementation implements AdminMapper {

    @Override
    public AdminResponse adminEntityToAdminResponse(Admin admin) {
        AdminResponse dto = new AdminResponse();

        return dto;
    }

    @Override
    public Admin adminRegistrationRequestToAdminEntity(AdminRegistrationRequest dto, Account account) {
        CommonPersonData data = new CommonPersonData();
        data.setLastname(dto.getLastname());
        data.setFirstname(dto.getFirstname());
        data.setPhonenumber(dto.getPhonenumber());
        data.setEmail(dto.getEmail());

        Admin admin = new Admin();
        admin.setAccount(account);
        admin.setData(data);
        return admin;
    }
}
