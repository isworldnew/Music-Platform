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
import ru.smirnov.musicplatform.config.MinioBuckets;
import ru.smirnov.musicplatform.dto.FileToUpdateDto;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistDataDto;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistToCreateDto;
import ru.smirnov.musicplatform.entity.auxiliary.enums.DistributionStatus;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.entity.relation.ArtistsSocialNetworks;
import ru.smirnov.musicplatform.exception.ArtistNameNonUniqueException;
import ru.smirnov.musicplatform.exception.FileSizeExcessException;
import ru.smirnov.musicplatform.mapper.ArtistMapper;
import ru.smirnov.musicplatform.repository.domain.ArtistRepository;
import ru.smirnov.musicplatform.service.minio.MinioService;
import ru.smirnov.musicplatform.service.sql.relation.ArtistSocialNetworkService;
import ru.smirnov.musicplatform.service.sql.relation.DistributorByArtistService;
import ru.smirnov.musicplatform.validators.enums.ContentType;
import ru.smirnov.musicplatform.validators.interfaces.FileValidator;

import java.util.HashMap;
import java.util.List;
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

    private final List<FileValidator> fileValidators;

    @Autowired
    public ArtistService(
            ArtistRepository artistRepository,
            DistributorByArtistService distributorByArtistService,
            ArtistSocialNetworkService artistSocialNetworkService,
            MinioService minioService,
            ArtistMapper artistMapper,
            List<FileValidator> fileValidators
    ) {
        this.artistRepository = artistRepository;
        this.distributorByArtistService = distributorByArtistService;
        this.artistSocialNetworkService = artistSocialNetworkService;
        this.minioService = minioService;
        this.artistMapper = artistMapper;
        this.fileValidators = fileValidators;
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
                throw new FileSizeExcessException("Cover's max size is" + (this.coverMaxSizeBytes / 1024.0) +  "KB, your is: " + dto.getCover().getSize() / 1024.0);
        }

        String objectName = dto.getName().replace(" ", "_");

        // вот от этого можно избавиться с помощью MapStruct или хотя бы примитивного Маппера:
        Artist artist = this.artistMapper.artistToCreateDtoToArtistEntity(dto, (MinioBuckets.ARTIST_COVER.getBucketName() + "/" + objectName));

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
                            objectName,
                            dto.getCover(),
                            null
                    );
                }
            });
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(artist.getId());

    }

//    @Transactional
//    public ResponseEntity<> updateArtistRootData(Long id, ArtistToUpdateDto dto) {
//
//        Artist artist = this.artistRepository.findById(id).orElse(null);
//
//        if (artist == null)
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//
//        if (this.artistRepository.findByName(dto.getName()).isPresent())
//            throw new ArtistNameNonUniqueException("Such artist's name already exists");
//
//        artist.setDescription(dto.getDescription());
//
//        String newName = dto.getName();
//        MultipartFile newCover = dto.getCover();
//
//        boolean coverIsAttached = (newCover != null && !newCover.isEmpty());
//        boolean nameHasChanged = (artist.getName().equals(newName));
//
//        if (!nameHasChanged && coverIsAttached) {
//            // взять старую ссылку
//            // сходить по ней в MinIO
//            // сохранить новый файл
//        }
//
//        if (!nameHasChanged && !coverIsAttached) {
//
//        }
//
//        if (nameHasChanged && !coverIsAttached) {
//
//        }
//
//        if (nameHasChanged && coverIsAttached) {
//
//        }
//
//
//
//    }

    // так... уже не нравится. Надо обновлять отдельно: имя и описание, файл (ну и ссылку), соцсети, статус взаимодействия

    public ResponseEntity<Void> updateArtistCover(Long id, FileToUpdateDto dto) {

        Artist artist = this.artistRepository.findById(id).orElse(null);

        if (artist == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        this.fileValidators.forEach(fileValidator -> fileValidator.validate(dto.getFile(), ContentType.IMAGE));

        boolean coverIsAttached = dto.getFile() != null && !dto.getFile().isEmpty();


        String[] cover = artist.getImageReference().split("/");
        String bucketName = cover[0];
        String objectName = cover[1];

        if (coverIsAttached)
            this.minioService.uploadObjectWithMetadata(
                    bucketName,
                    objectName,
                    dto.getFile(),
                    null
            );

        else this.minioService.removeObject(bucketName, objectName);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }



    public ResponseEntity<ArtistDataDto> getArtistDataById(Long id) {

        Artist artist = this.artistRepository.findById(id).orElse(null);

        if (artist == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        List<ArtistsSocialNetworks> rawArtistsSocialNetworks = this.artistSocialNetworkService.findAllByArtistId(artist.getId());
        Map<String, String> socialNetworks = new HashMap<>();

        for (ArtistsSocialNetworks asn : rawArtistsSocialNetworks)
            socialNetworks.put(asn.getSocialNetwork(), asn.getReference());

        return ResponseEntity.ok(this.artistMapper.createArtistDataDto(artist, socialNetworks));
    }
}
