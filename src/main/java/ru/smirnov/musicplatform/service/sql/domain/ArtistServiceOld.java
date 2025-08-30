package ru.smirnov.musicplatform.service.sql.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.config.MinioBuckets;
import ru.smirnov.musicplatform.dto.FileToUpdateDto;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistDataDto;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistExtendedDataDto;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistToCreateDto;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistToUpdateDto;
import ru.smirnov.musicplatform.dto.relation.ArtistSocialNetworkDto;
import ru.smirnov.musicplatform.entity.auxiliary.enums.DistributionStatus;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.entity.relation.ArtistsSocialNetworks;
import ru.smirnov.musicplatform.mapper.old.ArtistMapperOld;
import ru.smirnov.musicplatform.projection.DistributorByArtistProjection;
import ru.smirnov.musicplatform.repository.domain.ArtistRepository;
import ru.smirnov.musicplatform.service.SecurityContextServiceImpl;
import ru.smirnov.musicplatform.service.minio.MinioService;
import ru.smirnov.musicplatform.service.sql.relation.ArtistSocialNetworkServiceOld;
import ru.smirnov.musicplatform.service.sql.relation.DistributorByArtistServiceOld;
import ru.smirnov.musicplatform.util.MinioPathUtil;
import ru.smirnov.musicplatform.validators.old.ArtistValidator;
import ru.smirnov.musicplatform.validators.old.enums.ContentType;
import ru.smirnov.musicplatform.validators.old.interfaces.FileValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArtistServiceOld {

    private final SecurityContextServiceImpl securityContextService;

    private final ArtistRepository artistRepository;
    private final ArtistMapperOld artistMapper;

    private final DistributorByArtistServiceOld distributorByArtistService;
    private final ArtistSocialNetworkServiceOld artistSocialNetworkService;
    private final MinioService minioService;

    private final List<FileValidator> fileValidators;
    private final ArtistValidator artistValidator;

    private final MinioPathUtil minioPathUtil;

    @Autowired
    public ArtistServiceOld(
            ArtistRepository artistRepository,
            DistributorByArtistServiceOld distributorByArtistService,
            ArtistSocialNetworkServiceOld artistSocialNetworkService,
            MinioService minioService,
            ArtistMapperOld artistMapper,
            ArtistValidator artistValidator,
            List<FileValidator> fileValidators,
            MinioPathUtil minioPathUtil,
            SecurityContextServiceImpl securityContextService
    ) {
        this.artistRepository = artistRepository;
        this.distributorByArtistService = distributorByArtistService;
        this.artistSocialNetworkService = artistSocialNetworkService;
        this.minioService = minioService;
        this.artistMapper = artistMapper;
        this.artistValidator = artistValidator;
        this.fileValidators = fileValidators;
        this.minioPathUtil = minioPathUtil;
        this.securityContextService = securityContextService;
    }

    @Transactional
    public ResponseEntity<Long> createArtist(ArtistToCreateDto dto) {

        // валидации:
        // (в неявном виде каждая из строк может выбросить соответствующее исключение, которое обработает @ControllerAdvice)
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.artistValidator.existsByName(dto.getName());
        this.fileValidators.forEach(fileValidator -> fileValidator.validate(dto.getCover(), ContentType.IMAGE));

        boolean coverIsAttached = (dto.getCover() != null && !dto.getCover().isEmpty());

        String coverReference = this.minioPathUtil.generateFormattedReference(MinioBuckets.ARTIST_COVER.getBucketName(), dto.getName());

        // вот от этого можно избавиться с помощью MapStruct:
        Artist artist = this.artistMapper.artistToCreateDtoToArtistEntity(dto, coverReference);

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
                    minioService.uploadObjectWithMetadata( // this.minioService здесь уже не работает - другой контекст
                            MinioBuckets.ARTIST_COVER.getBucketName(),
                            minioPathUtil.extractObjectName(artist.getImageReference()),
                            dto.getCover(),
                            null
                    );
                }
            });
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(artist.getId());

    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ResponseEntity<ArtistExtendedDataDto> updateArtistCover(Long artistId, FileToUpdateDto dto) {

        // валидации:
        // (в неявном виде каждая из строк может выбросить соответствующее исключение, которое обработает @ControllerAdvice)
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.artistValidator.distributorAndItsArtistInteractionWithActiveStatus(tokenData.getEntityId(), artistId);
        Artist artist = this.artistValidator.safelyGetById(artistId);
        this.fileValidators.forEach(fileValidator -> fileValidator.validate(dto.getFile(), ContentType.IMAGE));

        /*
            После создания записи об Artist или обновления через этот метод, поле imageReference может быть:
            (1) null
            (2) заполненным
            В данный метод может прийти либо файл, который будет:
            [1] заполненным
            [2] null
        */

        boolean referenceExists = !(artist.getImageReference() == null || artist.getImageReference().isEmpty());
        boolean coverIsAttached = (dto.getFile() != null && !dto.getFile().isEmpty());

        // случай (1) и [2] даже не прописываю: там просто нечего делать

        // случай (1) и [1]
        if (!referenceExists && coverIsAttached) {
            artist.setImageReference(
                    this.minioPathUtil.generateFormattedReference(MinioBuckets.ARTIST_COVER.getBucketName(), artist.getName())
            );

            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    minioService.uploadObjectWithMetadata(
                            minioPathUtil.extractBucketName(artist.getImageReference()),
                            minioPathUtil.extractObjectName(artist.getImageReference()),
                            dto.getFile(),
                            null
                    );
                }
            });
        }

        // случай (2) и [1]
        if (referenceExists && coverIsAttached) { // просто перезаписать файл по существующей ссылке
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    minioService.uploadObjectWithMetadata(
                            minioPathUtil.extractBucketName(artist.getImageReference()),
                            minioPathUtil.extractObjectName(artist.getImageReference()),
                            dto.getFile(),
                            null
                    );
                }
            });
        }

        // случай (2) и [2]
        if (referenceExists && !coverIsAttached) {
            String bucketName = minioPathUtil.extractBucketName(artist.getImageReference());
            String objectName = minioPathUtil.extractObjectName(artist.getImageReference());
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    minioService.removeObject(
                            bucketName,
                            objectName
                    );
                }
            });
            artist.setImageReference(null);
        }

        this.artistRepository.save(artist);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                this.getArtistExtendedDataById(artistId).getBody()
        );

    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ResponseEntity<ArtistExtendedDataDto> updateArtistBasicData(Long artistId, ArtistToUpdateDto dto) {

        // валидации:
        // (в неявном виде каждая из строк может выбросить соответствующее исключение, которое обработает @ControllerAdvice)
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        Artist artist = this.artistValidator.safelyGetById(artistId);
        this.artistValidator.distributorAndItsArtistInteractionWithActiveStatus(tokenData.getEntityId(), artistId);
        this.artistValidator.existsByNameForUpdate(tokenData.getEntityId(), dto.getName());

        boolean nameHasChanged = !(artist.getName().equals(dto.getName()));


        artist.setName(dto.getName());
        artist.setDescription(dto.getDescription());

        if (nameHasChanged && artist.getImageReference() != null) {

            String oldReference = artist.getImageReference();
            String newReference = this.minioPathUtil.generateFormattedReference(MinioBuckets.ARTIST_COVER.getBucketName(), dto.getName());
            artist.setImageReference(newReference);

            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    minioService.replaceObjectInBucket(
                            minioPathUtil.extractBucketName(oldReference),
                            minioPathUtil.extractObjectName(oldReference),
                            minioPathUtil.extractObjectName(newReference)
                    );
                }
            });

        }

        this.artistRepository.save(artist);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                this.getArtistExtendedDataById(artistId).getBody()
        );

    }


    public ResponseEntity<ArtistDataDto> getArtistDataById(Long artistId) {

        Artist artist = this.artistRepository.findById(artistId).orElse(null);

        if (artist == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        List<ArtistsSocialNetworks> rawArtistsSocialNetworks = this.artistSocialNetworkService.findAllByArtistId(artist.getId());
        Map<String, String> socialNetworks = new HashMap<>();

        for (ArtistsSocialNetworks asn : rawArtistsSocialNetworks)
            socialNetworks.put(asn.getSocialNetwork(), asn.getReference());

        return ResponseEntity.ok(this.artistMapper.createArtistDataDto(artist, socialNetworks));
    }


    public ResponseEntity<ArtistExtendedDataDto> getArtistExtendedDataById(Long artistId) {

        Artist artist = this.artistValidator.safelyGetById(artistId);

        List<ArtistSocialNetworkDto> artistSocialNetworkDtos = this.artistSocialNetworkService.findAllArtistSocialNetworkDtoByArtistId(artistId);

        List<DistributorByArtistProjection> distributorByArtistProjections = this.distributorByArtistService.findDistributorByArtistProjectionByArtistId(artistId);

        return ResponseEntity.ok(
            this.artistMapper.artistEntityToArtistExtendedDataDto(artist, artistSocialNetworkDtos, distributorByArtistProjections)
        );
    }


}
