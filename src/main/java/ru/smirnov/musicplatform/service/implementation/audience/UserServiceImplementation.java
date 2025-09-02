package ru.smirnov.musicplatform.service.implementation.audience;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.audience.user.UserDataUpdateRequest;
import ru.smirnov.musicplatform.dto.audience.user.UserRegistrationRequest;
import ru.smirnov.musicplatform.dto.audience.user.UserResponse;
import ru.smirnov.musicplatform.entity.audience.Account;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.entity.auxiliary.embedding.CommonPersonData;
import ru.smirnov.musicplatform.entity.auxiliary.enums.AccountStatus;
import ru.smirnov.musicplatform.entity.auxiliary.enums.Role;
import ru.smirnov.musicplatform.exception.EmailOccupiedException;
import ru.smirnov.musicplatform.exception.PhonenumberOccupiedException;
import ru.smirnov.musicplatform.mapper.abstraction.UserMapper;
import ru.smirnov.musicplatform.precondition.abstraction.audience.UserPreconditionService;
import ru.smirnov.musicplatform.repository.audience.UserRepository;
import ru.smirnov.musicplatform.service.abstraction.audience.AccountService;
import ru.smirnov.musicplatform.service.abstraction.audience.UserService;

// ещё не переписал под интерфейсы
@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final AccountService accountService;

    private final UserPreconditionService userPreconditionService;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImplementation(
            UserRepository userRepository,
            AccountService accountService,
            UserPreconditionService userPreconditionService,
            UserMapper userMapper
            ) {
        this.userRepository = userRepository;
        this.accountService = accountService;
        this.userPreconditionService = userPreconditionService;
        this.userMapper = userMapper;
    }

    // такой уровень изоляции, потому что я хочу быть уверенным, что в рамках транзакции не появится
    // пользователей с таким же телефоном / почтой и аккаунтов с таким же юзернеймом
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public User userRegistration(UserRegistrationRequest dto) {

        // вот это всё хорошо бы прям вынести в какой-нибудь chain-of-responsibility...

        if (this.userRepository.findByPhonenumber(dto.getPhonenumber()).isPresent())
            throw new PhonenumberOccupiedException("This phonenumber is already in use by other user");

        if (this.userRepository.findByEmail(dto.getEmail()).isPresent())
            throw new EmailOccupiedException("This email is already in use by other user");


        // проверить, что пользователю есть, например, 14 лет


        Account newAccount = this.accountService.createAccount(dto.getAccountData(), Role.USER, AccountStatus.ENABLED);

        // вот это бы в какой-нибудь маппер вынести, хоть это и не совсем маппинг...
        CommonPersonData commonPersonData = new CommonPersonData();
        commonPersonData.setLastname(dto.getLastname());
        commonPersonData.setFirstname(dto.getFirstname());
        commonPersonData.setPhonenumber(dto.getPhonenumber());
        commonPersonData.setEmail(dto.getEmail());

        User user = new User();
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setData(commonPersonData);
        user.setAccount(newAccount);

        // пробовал прямо здесь сгенерировать токен на основании только что сохранённых данных и вернуть его
        // но я это делаю в рамках транзакции... А TokenGenerator внутри будет делать запрос к заблокированным транзакцией,
        // запущенной в данном методе, строкам (данным)... В итоге там мы ловим исключение о bad credentials...
        // но по факту там мы сталкиваемся с блокировкой и сразу возвращаем ошибку

        return this.userRepository.save(user);

    }

    @Override
    public UserResponse getUserData(DataForToken tokenData) {
        User user = this.userPreconditionService.findById(tokenData.getEntityId());
        return this.userMapper.userEntityToUserResponse(user);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void updateUserData(UserDataUpdateRequest dto, DataForToken tokenData) {
        /*уникальность номера телефона и почты*/
        User user = this.userPreconditionService.checkPhonenumberAndEmailUniqueness(dto.getPhonenumber(), dto.getEmail(), tokenData.getEntityId());

        CommonPersonData data = user.getData();

        data.setEmail(dto.getEmail());
        data.setPhonenumber(dto.getPhonenumber());
        data.setFirstname(dto.getLastname());
        data.setLastname(dto.getFirstname());

        user.setData(data);
        // можно проверить на возраст
        user.setDateOfBirth(dto.getDateOfBirth());

        this.userRepository.save(user);
    }

}
