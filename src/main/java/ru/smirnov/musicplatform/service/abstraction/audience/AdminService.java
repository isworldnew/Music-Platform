package ru.smirnov.musicplatform.service.abstraction.audience;

import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.audience.admin.AdminRequest;
import ru.smirnov.musicplatform.dto.audience.admin.AdminResponse;

public interface AdminService {

    // просмотр бизнес-данных об аккаунте
    AdminResponse getAdminData(DataForToken tokenData);

    // обновление данных об аккаунте
    void updateAdminData(AdminRequest dto, DataForToken tokenData);
}
