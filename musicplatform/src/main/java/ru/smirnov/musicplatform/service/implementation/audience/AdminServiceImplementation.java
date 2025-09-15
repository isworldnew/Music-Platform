package ru.smirnov.musicplatform.service.implementation.audience;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.dtoregistry.message.AdminDataMessage;
import ru.smirnov.musicplatform.dto.audience.admin.AdminRegistrationRequest;
import ru.smirnov.musicplatform.dto.audience.admin.AdminRequest;
import ru.smirnov.musicplatform.dto.audience.admin.AdminResponse;
import ru.smirnov.musicplatform.entity.audience.Account;
import ru.smirnov.musicplatform.entity.audience.Admin;
import ru.smirnov.dtoregistry.entity.auxiliary.AccountStatus;
import ru.smirnov.musicplatform.entity.auxiliary.embedding.CommonPersonData;
import ru.smirnov.musicplatform.entity.auxiliary.enums.Role;
import ru.smirnov.musicplatform.kafka.producer.abstraction.KafkaAdminProducer;
import ru.smirnov.musicplatform.mapper.abstraction.AdminMapper;
import ru.smirnov.musicplatform.precondition.abstraction.audience.AdminPreconditionService;
import ru.smirnov.musicplatform.repository.audience.AdminRepository;
import ru.smirnov.musicplatform.service.abstraction.audience.AccountService;
import ru.smirnov.musicplatform.service.abstraction.audience.AdminService;

import java.util.List;

@Service
public class AdminServiceImplementation implements AdminService {

    private final AdminRepository adminRepository;
    private final AdminPreconditionService adminPreconditionService;
    private final AdminMapper adminMapper;
    private final AccountService accountService;
    private final KafkaAdminProducer kafkaAdminProducer;

    @Autowired
    public AdminServiceImplementation(
            AdminRepository adminRepository,
            AdminPreconditionService adminPreconditionService,
            AdminMapper adminMapper,
            AccountService accountService, KafkaAdminProducer kafkaAdminProducer
    ) {
        this.adminRepository = adminRepository;
        this.adminPreconditionService = adminPreconditionService;
        this.adminMapper = adminMapper;
        this.accountService = accountService;
        this.kafkaAdminProducer = kafkaAdminProducer;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Long adminRegistration(AdminRegistrationRequest dto) {
        this.adminPreconditionService.checkExistenceByPhonenumberOrEmail(dto.getPhonenumber(), dto.getEmail());

        Account account = this.accountService.createAccount(dto.getAccountData(), Role.ADMIN, AccountStatus.ENABLED, false);
        Admin admin = this.adminMapper.adminRegistrationRequestToAdminEntity(dto, account);

        this.adminRepository.save(admin);

        // и вот тут нужно будет вызвать продюсер, чтобы отправить в него сообщение
        this.kafkaAdminProducer.sendMessage(new AdminDataMessage(admin.getId(), admin.getAccount().getStatus()));

        return admin.getId();
    }

    @Override
    public AdminResponse getAdminData(DataForToken tokenData) {
        Admin admin = this.adminPreconditionService.getByIdIfExists(tokenData.getEntityId());
        return this.adminMapper.adminEntityToAdminResponse(admin);
    }

    @Override
    public void updateAdminData(AdminRequest dto, DataForToken tokenData) {
        Admin admin = this.adminPreconditionService.getByIdIfExists(tokenData.getEntityId());

        CommonPersonData data = new CommonPersonData();
        data.setLastname(dto.getLastname());
        data.setFirstname(dto.getFirstname());
        data.setPhonenumber(dto.getPhonenumber());
        data.setEmail(dto.getEmail());
        admin.setData(data);

        this.adminRepository.save(admin);
    }

    @Override
    public List<Long> getAllEnabledAdmins() {
        return this.adminRepository.findAllEnabledAdmins();
    }
}
