package ru.smirnov.musicplatform.service.sql.audience;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.dto.audience.user.UserPersonalDataRegistrationDto;
import ru.smirnov.musicplatform.entity.audience.Account;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.entity.auxiliary.embedding.CommonPersonData;
import ru.smirnov.musicplatform.entity.auxiliary.enums.AccountStatus;
import ru.smirnov.musicplatform.entity.auxiliary.enums.Role;
import ru.smirnov.musicplatform.exception.EmailOccupiedException;
import ru.smirnov.musicplatform.exception.PhonenumberOccupiedException;
import ru.smirnov.musicplatform.repository.audience.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountService accountService;

    @Autowired
    public UserService(UserRepository userRepository, AccountService accountService) {
        this.userRepository = userRepository;
        this.accountService = accountService;
    }

    // такой уровень изоляции, потому что я хочу быть уверенным, что в рамках транзакции не появится
    // пользователей с таким же телефоном / почтой и аккаунтов с таким же юзернеймом
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public User userRegistration(UserPersonalDataRegistrationDto dto) {

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

}
