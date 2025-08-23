package ru.smirnov.musicplatform.service.sql.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.ArtistToCreateDto;
import ru.smirnov.musicplatform.entity.auxiliary.enums.DistributionStatus;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.exception.ArtistNameNonUniqueException;
import ru.smirnov.musicplatform.exception.CoverSizeExcessException;
import ru.smirnov.musicplatform.mapper.ArtistMapper;
import ru.smirnov.musicplatform.repository.domain.ArtistRepository;
import ru.smirnov.musicplatform.service.minio.MinioService;
import ru.smirnov.musicplatform.service.sql.relation.ArtistSocialNetworkService;
import ru.smirnov.musicplatform.service.sql.relation.DistributorByArtistService;

import java.util.Map;

@Service
public class ArtistService {

    @Value("${cover.max.size.bytes}")
    private Long coverMaxSizeBytes;

    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;

    private final DistributorByArtistService distributorByArtistService;
    private final ArtistSocialNetworkService artistSocialNetworkService;
    private final MinioService minioService;

    @Autowired
    public ArtistService(
            ArtistRepository artistRepository,
            DistributorByArtistService distributorByArtistService,
            ArtistSocialNetworkService artistSocialNetworkService,
            MinioService minioService,
            ArtistMapper artistMapper
    ) {
        this.artistRepository = artistRepository;
        this.distributorByArtistService = distributorByArtistService;
        this.artistSocialNetworkService = artistSocialNetworkService;
        this.minioService = minioService;
        this.artistMapper = artistMapper;
    }

    @Transactional
    public ResponseEntity<Long> createArtist(ArtistToCreateDto dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DataForToken tokenData = (DataForToken) authentication.getPrincipal();

        if (this.artistRepository.findByName(dto.getName()).isPresent())
            throw new ArtistNameNonUniqueException("Such artist's name already exists");

        boolean coverIsAttached = false;

        if (dto.getCover() != null && !dto.getCover().isEmpty()) {
            coverIsAttached = true;
            if (dto.getCover().getSize() > this.coverMaxSizeBytes)
                throw new CoverSizeExcessException("Cover's max size is 500 KB, your is: " + dto.getCover().getSize() / 1024.0);
        }

        String objectName = dto.getName().replace(" ", "_");

        // вот от этого можно избавиться с помощью MapStruct или хотя бы примитивного Маппера:
        Artist artist = this.artistMapper.artistToCreateDtoToArtistEntity(dto, "artist-cover" + "/" + objectName);

        this.artistRepository.save(artist);

        this.distributorByArtistService.save(tokenData.getEntityId(), artist.getId(), DistributionStatus.ACTIVE);

        Map<String, String> socialNetworks = dto.getSocialNetworks();

        if (socialNetworks != null && !socialNetworks.isEmpty()) {
            for (String socialNetworkName : socialNetworks.keySet())
                this.artistSocialNetworkService.save(artist.getId(), socialNetworkName, socialNetworks.get(socialNetworkName));
        }

        if (coverIsAttached) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    minioService.uploadObjectWithMetadata( // this здесь уже не работает - другой контекст
                            "artist-cover",
                            objectName,
                            dto.getCover(),
                            null
                    );
                }
            });
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(artist.getId());

    }

}
