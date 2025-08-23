package ru.smirnov.musicplatform.service.sql.domain;

import io.minio.messages.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.config.MinioBuckets;
import ru.smirnov.musicplatform.dto.FileToUpdateDto;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistDataDto;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistToCreateDto;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistToUpdateDto;
import ru.smirnov.musicplatform.entity.auxiliary.enums.DistributionStatus;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.entity.relation.ArtistsSocialNetworks;
import ru.smirnov.musicplatform.entity.relation.DistributorsByArtists;
import ru.smirnov.musicplatform.exception.ArtistNameNonUniqueException;
import ru.smirnov.musicplatform.exception.FileSizeExcessException;
import ru.smirnov.musicplatform.mapper.ArtistMapper;
import ru.smirnov.musicplatform.repository.domain.ArtistRepository;
import ru.smirnov.musicplatform.service.minio.MinioService;
import ru.smirnov.musicplatform.service.sql.relation.ArtistSocialNetworkService;
import ru.smirnov.musicplatform.service.sql.relation.DistributorByArtistService;
import ru.smirnov.musicplatform.validators.ArtistValidator;
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
    private final ArtistValidator artistValidator;

    @Autowired
    public ArtistService(
            ArtistRepository artistRepository,
            DistributorByArtistService distributorByArtistService,
            ArtistSocialNetworkService artistSocialNetworkService,
            MinioService minioService,
            ArtistMapper artistMapper,
            ArtistValidator artistValidator,
            List<FileValidator> fileValidators
    ) {
        this.artistRepository = artistRepository;
        this.distributorByArtistService = distributorByArtistService;
        this.artistSocialNetworkService = artistSocialNetworkService;
        this.minioService = minioService;
        this.artistMapper = artistMapper;
        this.artistValidator = artistValidator;
        this.fileValidators = fileValidators;
    }

    @Transactional
    public ResponseEntity<Long> createArtist(ArtistToCreateDto dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DataForToken tokenData = (DataForToken) authentication.getPrincipal();

//        if (this.artistRepository.findByName(dto.getName()).isPresent())
//            throw new ArtistNameNonUniqueException("Such artist's name already exists");

        // валидации:
        this.artistValidator.existsByName(dto.getName());
        this.fileValidators.forEach(fileValidator -> fileValidator.validate(dto.getCover(), ContentType.IMAGE));

        boolean coverIsAttached = (dto.getCover() != null && !dto.getCover().isEmpty());

//        if (dto.getCover() != null && !dto.getCover().isEmpty()) {
//            coverIsAttached = true;
//            if (dto.getCover().getSize() > this.coverMaxSizeBytes)
//                throw new FileSizeExcessException("Cover's max size is" + (this.coverMaxSizeBytes / 1024.0) +  "KB, your is: " + dto.getCover().getSize() / 1024.0);
//        }

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

    public ResponseEntity<Void> updateArtistCover(Long artistId, FileToUpdateDto dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DataForToken tokenData = (DataForToken) authentication.getPrincipal();

        // дистрибьютор может редактировать только своих исполнителей:
        // поэтому проверяю, что я обращаюсь именно к своему исполнителю, с которым ACTIVE-ная связь
        Long distributorId = this.distributorByArtistService.activeDistributionStatusWithArtist(artistId).orElse(null);
        if (!tokenData.getEntityId().equals(distributorId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        // валидации:
        Artist artist = this.artistValidator.safelyGetById(artistId);
        this.fileValidators.forEach(fileValidator -> fileValidator.validate(dto.getFile(), ContentType.IMAGE));

        boolean coverIsAttached = (dto.getFile() != null && !dto.getFile().isEmpty());

        if (coverIsAttached) {

            if (artist.getImageReference() != null)
                this.minioService.uploadObjectWithMetadata(
                        artist.getBucketName(),
                        artist.getObjectName(),
                        dto.getFile(),
                        null
                );
            else {
                // после удаления изображения нужно заново сгенерировать ссылку
                this.minioService.uploadObjectWithMetadata(
                        artist.getBucketName(),
                        artist.getObjectName(),
                        dto.getFile(),
                        null
                );
                artist.setImageReference(MinioBuckets.ARTIST_COVER.getBucketName() + "/" + /*вот тут допиши с нижним подчёркиванием*/);
            }
        }

        else {
            this.minioService.removeObject(artist.getBucketName(), artist.getObjectName());
            artist.setImageReference(null);
            this.artistRepository.save(artist);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    // пока оставлю Void, но вообще желательно возвращать ту сущность, что я буду возвращать Дистрибьютору при чтении информации об Исполнителе
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ResponseEntity<Void> updateArtistBasicData(Long artistId, ArtistToUpdateDto dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DataForToken tokenData = (DataForToken) authentication.getPrincipal();

        // валидации:
        Artist artist = this.artistValidator.safelyGetById(artistId);
        this.artistValidator.existsByNameForUpdate(artist.getId(), dto.getName());

        // дистрибьютор может редактировать только своих исполнителей:
        // поэтому проверяю, что я обращаюсь именно к своему исполнителю, с которым ACTIVE-ная связь
        Long distributorId = this.distributorByArtistService.activeDistributionStatusWithArtist(artistId).orElse(null);
        if (!tokenData.getEntityId().equals(distributorId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        artist.setName(dto.getName());
        artist.setDescription(dto.getDescription());

        this.artistRepository.save(artist);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

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
}
