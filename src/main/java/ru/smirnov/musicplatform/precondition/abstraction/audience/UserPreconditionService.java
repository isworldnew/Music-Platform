package ru.smirnov.musicplatform.precondition.abstraction.audience;

import ru.smirnov.musicplatform.entity.audience.User;

public interface UserPreconditionService {

    User findById(Long userId);

    User checkPhonenumberAndEmailUniqueness(String phonenumber, String email, Long userId);
}
