package ru.smirnov.musicplatform.precondition.implementation.audience;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.exception.EmailOccupiedException;
import ru.smirnov.musicplatform.exception.NotFoundException;
import ru.smirnov.musicplatform.exception.PhonenumberOccupiedException;
import ru.smirnov.musicplatform.precondition.abstraction.audience.UserPreconditionService;
import ru.smirnov.musicplatform.repository.audience.UserRepository;

@Service
public class UserPreconditionServiceImplementation implements UserPreconditionService {

    private final UserRepository userRepository;

    @Autowired
    public UserPreconditionServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(Long userId) {
        return this.userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id=" + userId + " was not found")
        );
    }

    @Override
    public User checkPhonenumberAndEmailUniqueness(String phonenumber, String email, Long userId) {
        User userFoundById = this.findById(userId);

        boolean phonenumberMatches = userFoundById.getData().getPhonenumber().equals(phonenumber);
        boolean emailMatches = userFoundById.getData().getEmail().equals(email);

        // значит, пользователь решил их не обновлять
        if (phonenumberMatches && emailMatches)
            return userFoundById;

        User userFoundByPhonenumber = this.userRepository.findByPhonenumber(phonenumber).orElse(null);
        User userFoundByEmail = this.userRepository.findByEmail(email).orElse(null);

        if (userFoundByPhonenumber != null && !userFoundByPhonenumber.getId().equals(userId))
            throw new PhonenumberOccupiedException("Phonenumber '" + phonenumber + "' is occupied by user (username='" + userFoundByPhonenumber.getAccount().getUsername() + "')");


        if (userFoundByEmail != null && !userFoundByPhonenumber.getId().equals(userId))
            throw new EmailOccupiedException("Email '" + email + "' is occupied by user (username='" + userFoundByEmail.getAccount().getUsername() + "')");

        return userFoundById;
    }
}
