package ru.smirnov.musicplatform.mapper.implementation;

import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.audience.admin.AdminResponse;
import ru.smirnov.musicplatform.entity.audience.Admin;
import ru.smirnov.musicplatform.mapper.abstraction.AdminMapper;

@Component
public class AdminMapperImplementation implements AdminMapper {

    @Override
    public AdminResponse adminEntityToAdminResponse(Admin admin) {
        AdminResponse dto = new AdminResponse();

        return dto;
    }
}
