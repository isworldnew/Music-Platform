package ru.smirnov.musicplatform.service.implementation.audience;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.dtoregistry.entity.auxiliary.AccountStatus;
import ru.smirnov.dtoregistry.entity.auxiliary.DistributorType;
import ru.smirnov.dtoregistry.message.DistributorRegistrationMessage;
import ru.smirnov.musicplatform.dto.audience.distributor.DistributorRequest;
import ru.smirnov.musicplatform.dto.audience.distributor.DistributorResponse;
import ru.smirnov.musicplatform.entity.audience.Account;
import ru.smirnov.musicplatform.entity.audience.Distributor;
import ru.smirnov.musicplatform.entity.auxiliary.enums.Role;
import ru.smirnov.musicplatform.mapper.abstraction.DistributorMapper;
import ru.smirnov.musicplatform.precondition.abstraction.audience.DistributorPreconditionService;
import ru.smirnov.musicplatform.repository.audience.DistributorRepository;
import ru.smirnov.musicplatform.service.abstraction.audience.AccountService;
import ru.smirnov.musicplatform.service.abstraction.audience.DistributorService;

@Service
public class DistributorServiceImplementation implements DistributorService {

    private final DistributorRepository distributorRepository;
    private final DistributorPreconditionService distributorPreconditionService;
    private final DistributorMapper distributorMapper;
    private final AccountService accountService;

    @Autowired
    public DistributorServiceImplementation(
            DistributorRepository distributorRepository,
            DistributorPreconditionService distributorPreconditionService,
            DistributorMapper distributorMapper, AccountService accountService
    ) {
        this.distributorRepository = distributorRepository;
        this.distributorPreconditionService = distributorPreconditionService;
        this.distributorMapper = distributorMapper;
        this.accountService = accountService;
    }

    @Override
    public void updateDistributorData(DistributorRequest dto, DataForToken tokenData) {
//        Distributor distributor = this.distributorPreconditionService.nameUniquenessDuringUpdate(tokenData.getEntityId(), dto.getName());

        Distributor distributor = this.distributorPreconditionService.getByIdIfExists(tokenData.getEntityId());

        distributor.setName(dto.getName());
        distributor.setDescription(dto.getDescription());
        distributor.setDistributorType(DistributorType.valueOf(dto.getDistributorType()));

        this.distributorRepository.save(distributor);
    }

    @Override
    public DistributorResponse getDistributorData(DataForToken tokenData) {
        Distributor distributor = this.distributorPreconditionService.getByIdIfExists(tokenData.getEntityId());
        return this.distributorMapper.distributorEntityToDistributorResponse(distributor);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Long distributorRegistration(DistributorRegistrationMessage message) {
        Account account = this.accountService.createAccount(message.getAccountData(), Role.DISTRIBUTOR, AccountStatus.ENABLED);

        Distributor distributor = this.distributorMapper.createDistributorEntity(account, message);

        return this.distributorRepository.save(distributor).getId();
    }

}
