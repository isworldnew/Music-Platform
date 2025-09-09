package ru.smirnov.musicplatform.service.abstraction.audience;

import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.audience.admin.AdminRegistrationRequest;
import ru.smirnov.musicplatform.dto.audience.admin.AdminRequest;
import ru.smirnov.musicplatform.dto.audience.admin.AdminResponse;

import java.util.List;

public interface AdminService {

    Long adminRegistration(AdminRegistrationRequest dto);

    // просмотр бизнес-данных об аккаунте
    AdminResponse getAdminData(DataForToken tokenData);

    // обновление данных об аккаунте
    void updateAdminData(AdminRequest dto, DataForToken tokenData);

    List<Long> getAllEnabledAdmins();
}
