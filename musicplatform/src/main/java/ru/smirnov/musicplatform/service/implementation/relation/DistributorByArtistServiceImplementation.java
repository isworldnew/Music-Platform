package ru.smirnov.musicplatform.service.implementation.relation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.relation.DistributorByArtistRelationRequest;
import ru.smirnov.musicplatform.entity.auxiliary.enums.DistributionStatus;
import ru.smirnov.musicplatform.entity.auxiliary.enums.Role;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.entity.relation.DistributorsByArtists;
import ru.smirnov.musicplatform.exception.BadRequestException;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.exception.NotFoundException;
import ru.smirnov.musicplatform.precondition.abstraction.domain.ArtistPreconditionService;
import ru.smirnov.musicplatform.precondition.abstraction.relation.DistributorByArtistPreconditionService;
import ru.smirnov.musicplatform.repository.relation.DistributorByArtistRepository;
import ru.smirnov.musicplatform.service.abstraction.relation.DistributorByArtistService;

import java.util.List;

@Slf4j
@Service
public class DistributorByArtistServiceImplementation implements DistributorByArtistService  {

    private final DistributorByArtistRepository distributorByArtistRepository;
    private final DistributorByArtistPreconditionService distributorByArtistPreconditionService;
    private final ArtistPreconditionService artistPreconditionService;

    @Autowired
    public DistributorByArtistServiceImplementation(
            DistributorByArtistRepository distributorByArtistRepository,
            DistributorByArtistPreconditionService distributorByArtistPreconditionService, ArtistPreconditionService artistPreconditionService
    ) {
        this.distributorByArtistRepository = distributorByArtistRepository;
        this.distributorByArtistPreconditionService = distributorByArtistPreconditionService;
        this.artistPreconditionService = artistPreconditionService;
    }

    @Override
    @Transactional
    public Long save(Long distributorId, Long artistId, DistributionStatus status) {

        // вот тут дописать побольше проверок

        return this.distributorByArtistRepository.save(distributorId, artistId, status.name());
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void updateRelationBetweenDistributorAndArtist(Long artistId, DistributorByArtistRelationRequest dto, DataForToken tokenData) {

        Artist artist = this.artistPreconditionService.getByIdIfExists(artistId);
        DistributionStatus status = DistributionStatus.valueOf(dto.getDistributionStatus());

        if (tokenData.getRole().equals(Role.DISTRIBUTOR.name())) {
            if (status.equals(DistributionStatus.SUSPENDED_BY_ADMIN))
                throw new BadRequestException("Distributor is unable to use '" + DistributionStatus.SUSPENDED_BY_ADMIN.name() + "' status to relation with artist");

            if (status.isDeactivating()) {
                // мы можем хотеть с нашим ACTIVE-исполнителем разорвать связь (деактивировать её):
                // тогда надо проверить, что мы с ним эту связь имеем
                this.distributorByArtistPreconditionService.checkActiveRelationBetweenDistributorAndArtistExistence(tokenData.getEntityId(), artist.getId());
                DistributorsByArtists relation = this.distributorByArtistRepository.findByDistributorAndArtist(tokenData.getEntityId(), artist.getId()).get();

                relation.setStatus(DistributionStatus.valueOf(dto.getDistributionStatus()));
                this.distributorByArtistRepository.save(relation);
                return;
            }

            if (!status.isDeactivating()) {
                // мы можем хотеть восстановить связь с исполнителем, с которым связь была разорвана (если у него нет новых ACTIVE-связей)
                // восстановление возможно, только если сейчас исполнителем никто не управляет и если связь преравана не администратором
                List<DistributorsByArtists> relations = this.distributorByArtistRepository.findRelationsOfArtist(artist.getId());

                boolean hasActiveRelation = (!(relations.stream().filter(relation -> relation.getStatus().equals(DistributionStatus.ACTIVE)).toList()).isEmpty());

                if (hasActiveRelation)
                    throw new ConflictException("Artist (id=" + artistId + ") already has new ACTIVE status");

                boolean wasDistributed = (relations.stream().filter(relation -> relation.getDistributor().getId().equals(tokenData.getEntityId())).count() == 1);

                if (!wasDistributed)
                    throw new BadRequestException("Artist (id=" + artistId + ") was never distributed by Distributor (id=" + tokenData.getEntityId() + ")");

                DistributorsByArtists relation = this.distributorByArtistRepository.findByDistributorAndArtist(tokenData.getEntityId(), artist.getId()).get();

                if (relation.getStatus().equals(DistributionStatus.SUSPENDED_BY_ADMIN))
                    throw new ForbiddenException("Relation was suspended by admin, unable to fix it by ditributor");

                relation.setStatus(DistributionStatus.valueOf(dto.getDistributionStatus()));
                this.distributorByArtistRepository.save(relation);
                return;
            }
        }

        // иначе - это администратор

        boolean hasNoActiveRelations = this.distributorByArtistRepository.findRelationsOfArtist(artist.getId()).stream()
                .filter(relation -> relation.getStatus().equals(DistributionStatus.ACTIVE))
                .toList()
                .isEmpty();

        if (!status.isDeactivating() && !hasNoActiveRelations)
            throw new ConflictException("Artist (id=" + artistId + ") already has ACTIVE relation status");

        DistributorsByArtists relation = this.distributorByArtistRepository.findByDistributorAndArtist(dto.getDistributorId(), artist.getId()).orElseThrow(
                () -> new NotFoundException("Relation between artist (id=" + artistId + ") and distributor (id=" + dto.getDistributorId() + ") was not found")
        );

        relation.setStatus(DistributionStatus.valueOf(dto.getDistributionStatus()));
        this.distributorByArtistRepository.save(relation);
    }

}
