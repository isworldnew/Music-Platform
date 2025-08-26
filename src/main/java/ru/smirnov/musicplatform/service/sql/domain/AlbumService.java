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
import ru.smirnov.musicplatform.dto.domain.album.MusicCollectionToCreateDto;
import ru.smirnov.musicplatform.entity.domain.Album;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.mapper.AlbumMapper;
import ru.smirnov.musicplatform.repository.domain.AlbumRepository;
import ru.smirnov.musicplatform.service.SecurityContextService;
import ru.smirnov.musicplatform.service.minio.MinioService;
import ru.smirnov.musicplatform.service.sql.relation.TrackByAlbumService;
import ru.smirnov.musicplatform.util.MinioPathUtil;
import ru.smirnov.musicplatform.validators.AlbumValidator;
import ru.smirnov.musicplatform.validators.ArtistValidatorImproved;
import ru.smirnov.musicplatform.validators.TrackValidator;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;

    private final SecurityContextService securityContextService;

    private final MinioService minioService;
    private final MinioPathUtil minioPathUtil;

    private final ArtistValidatorImproved artistValidator;
    private final AlbumValidator albumValidator;
    private final TrackValidator trackValidator;

    private final TrackByAlbumService trackByAlbumService;

    @Autowired
    public AlbumService(
            AlbumRepository albumRepository,
            AlbumMapper albumMapper,
            SecurityContextService securityContextService,
            MinioService minioService,
            MinioPathUtil minioPathUtil,
            ArtistValidatorImproved artistValidator,
            AlbumValidator albumValidator,
            TrackValidator trackValidator,
            TrackByAlbumService trackByAlbumService
    ) {
        this.albumRepository = albumRepository;
        this.albumMapper = albumMapper;
        this.securityContextService = securityContextService;
        this.minioService = minioService;
        this.minioPathUtil = minioPathUtil;
        this.artistValidator = artistValidator;
        this.albumValidator = albumValidator;
        this.trackValidator = trackValidator;
        this.trackByAlbumService = trackByAlbumService;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ResponseEntity<Long> createAlbum(MusicCollectionToCreateDto dto) {

        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();

        boolean tracksAttached = (dto.getTracks() != null && !dto.getTracks().isEmpty());
        boolean coverAttached = (dto.getCover() != null && !dto.getCover().isEmpty());

        Artist artist = null;

        // вот тут пройдут в принципе все нужные провеки на то, что:
        // [v] данный исполнитель существует
        // [v] данный дистрибьютор имеет ACTIVE-ную связь с данным исполнителем
        // [v] если передали какие-то треки, то все они существуют
        // [v] относятся к данному исполнителю (его собственные или он - соавтор)
        if (tracksAttached) {
            dto.getTracks().forEach(track -> this.trackValidator.safelyGetById(track));
            artist = this.artistValidator.distributorIsAbleToInteractWithThisArtist(tokenData.getEntityId(), dto.getCreatorId(), dto.getTracks());
        }
        else
            artist = this.artistValidator.distributorIsAbleToInteractWithThisArtist(tokenData.getEntityId(), dto.getCreatorId());

        // [v] дополнительно проверяю, что у данного исполнителя ещё нет альбома с таким названием
        this.albumValidator.checkAlbumNameUniquenessForArtist(dto.getName(), dto.getCreatorId());

        Album album = this.albumMapper.musicCollectionToCreateDtoToAlbumEntity(dto, artist);

        // раннее сохранение, чтобы у нас был id альбома, который нужен
        // для создания ссылки на обложку и для сохранения треков в альбом
        this.albumRepository.save(album);

        if (tracksAttached)
            this.trackByAlbumService.addTracksToAlbum(album.getId(), dto.getTracks().stream().toList());


        if (coverAttached) {
            // ссылка будет составляться из artist_id и album_id
            String coverReference = this.minioPathUtil.generateFormattedReference(MinioBuckets.ALBUM_COVER.getBucketName(), artist.getId(), album.getId());

            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    minioService.uploadObjectWithMetadata(
                            MinioBuckets.ALBUM_COVER.getBucketName(),
                            minioPathUtil.extractObjectName(coverReference),
                            dto.getCover(),
                            null
                    );
                }
            });

            album.setImageReference(coverReference);
            this.albumRepository.save(album);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(album.getId());
    }

}
