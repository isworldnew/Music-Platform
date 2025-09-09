package ru.smirnov.musicplatform.precondition.abstraction.audience;

import ru.smirnov.musicplatform.entity.audience.Admin;

public interface AdminPreconditionService {

    Admin getByIdIfExists(Long adminId);

    void checkExistenceByPhonenumberOrEmail(String phonenumber, String email);
}
