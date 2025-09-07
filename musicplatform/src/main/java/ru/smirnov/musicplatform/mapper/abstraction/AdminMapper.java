package ru.smirnov.musicplatform.mapper.abstraction;

import ru.smirnov.musicplatform.dto.audience.admin.AdminResponse;
import ru.smirnov.musicplatform.entity.audience.Admin;

public interface AdminMapper {

    AdminResponse adminEntityToAdminResponse(Admin admin);
}
