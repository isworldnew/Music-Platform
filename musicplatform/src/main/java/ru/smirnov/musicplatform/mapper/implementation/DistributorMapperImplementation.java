package ru.smirnov.musicplatform.mapper.implementation;

import org.springframework.stereotype.Component;
import ru.smirnov.dtoregistry.message.DistributorRegistrationMessage;
import ru.smirnov.musicplatform.dto.audience.distributor.DistributedArtistShortcutResponse;
import ru.smirnov.musicplatform.dto.audience.distributor.DistributorResponse;
import ru.smirnov.musicplatform.dto.audience.distributor.DistributorShortcutResponse;
import ru.smirnov.musicplatform.dto.audience.distributor.ExtendedDistributorResponse;
import ru.smirnov.musicplatform.entity.audience.Account;
import ru.smirnov.musicplatform.entity.audience.Distributor;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.mapper.abstraction.DistributorMapper;

import java.util.ArrayList;
import java.util.List;

@Component
public class DistributorMapperImplementation implements DistributorMapper {

    @Override
    public DistributorResponse distributorEntityToDistributorResponse(Distributor distributor) {
        DistributorResponse dto = new DistributorResponse();
        dto.setDistributorId(distributor.getId());
        dto.setAccountId(distributor.getAccount().getId());
        dto.setUsername(distributor.getAccount().getUsername());
        dto.setName(distributor.getName());
        dto.setDescription(distributor.getDescription());
        dto.setDistributorType(distributor.getDistributorType().name());
        dto.setRegistrationDate(distributor.getRegistrationDate().toString());
        return dto;
    }

    @Override
    public DistributorShortcutResponse distributorEntityToDistributorShortcutResponse(Distributor distributor) {
        DistributorShortcutResponse dto = new DistributorShortcutResponse();
        dto.setId(distributor.getId());
        dto.setName(distributor.getName());
        dto.setDistributorType(distributor.getDistributorType().name());
        return dto;
    }

    @Override
    public ExtendedDistributorResponse distributorEntityToExtendedDistributorResponse(Distributor distributor) {
        ExtendedDistributorResponse dto = new ExtendedDistributorResponse();
        dto.setId(distributor.getId());
        dto.setName(distributor.getName());
        dto.setDescription(distributor.getDescription());
        dto.setDistributorType(distributor.getDistributorType().name());
        dto.setRegistrationDate(distributor.getRegistrationDate().toString());

        if (distributor.getArtists() != null && !distributor.getArtists().isEmpty()) {
            List<DistributedArtistShortcutResponse> distributedArtists = new ArrayList<>();

            distributor.getArtists().forEach(relation -> distributedArtists.add(
                    new DistributedArtistShortcutResponse(
                            relation.getId(),
                            relation.getArtist().getId(),
                            relation.getArtist().getName(),
                            relation.getStatus().name()
                    )
            ));

            dto.setDistributedArtists(distributedArtists);
        }

        return dto;
    }

    @Override
    public Distributor createDistributorEntity(Account account, DistributorRegistrationMessage message) {
        Distributor distributor = new Distributor();
        distributor.setName(message.getName());
        distributor.setDescription(message.getDescription());
        distributor.setDistributorType(message.getDistributorType());
        distributor.setAccount(account);
        return distributor;
    }
}
