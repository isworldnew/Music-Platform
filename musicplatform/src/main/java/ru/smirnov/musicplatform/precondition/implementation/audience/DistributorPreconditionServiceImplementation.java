package ru.smirnov.musicplatform.precondition.implementation.audience;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.entity.audience.Distributor;
import ru.smirnov.musicplatform.exception.NotFoundException;
import ru.smirnov.musicplatform.precondition.abstraction.audience.DistributorPreconditionService;
import ru.smirnov.musicplatform.repository.audience.DistributorRepository;

@Service
public class DistributorPreconditionServiceImplementation implements DistributorPreconditionService {

    private final DistributorRepository distributorRepository;

    @Autowired
    public DistributorPreconditionServiceImplementation(DistributorRepository distributorRepository) {
        this.distributorRepository = distributorRepository;
    }

    @Override
    public Distributor getByIdIfExists(Long distributorId) {
        return this.distributorRepository.findById(distributorId).orElseThrow(
                () -> new NotFoundException("Distributor with id=" + distributorId + " was not found")
        );
    }

    // зачем ему уникальное имя? достаточно уникального username
//    @Override
//    public Distributor nameUniquenessDuringUpdate(Long distributorId, String name) {
//        Distributor distributorFoundById = this.getByIdIfExists(distributorId);
//
//        if (distributorFoundById.getName().equals(name))
//            return distributorFoundById;
//
//        Distributor distributorFoundByName = this.distributorRepository.findByName(name).orElse(null);
//
//        if (distributorFoundByName != null)
//            throw new ConflictException("Name '" + name + "' is occupied by distributor (id=" + distributorFoundByName.getId() + ")");
//
//        return distributorFoundById;
//    }
}
