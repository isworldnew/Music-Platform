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
import ru.smirnov.musicplatform.dto.domain.MusicCollectionAuthorDto;
import ru.smirnov.musicplatform.dto.domain.album.*;
import ru.smirnov.musicplatform.dto.domain.track.TrackShortcutDto;
import ru.smirnov.musicplatform.entity.audience.Admin;
import ru.smirnov.musicplatform.entity.auxiliary.enums.MusicCollectionAccessLevel;
import ru.smirnov.musicplatform.entity.domain.Chart;
import ru.smirnov.musicplatform.entity.domain.Track;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.mapper.old.MusicCollectionMapperOld;
import ru.smirnov.musicplatform.repository.audience.AdminRepository;
import ru.smirnov.musicplatform.repository.domain.ChartRepository;
import ru.smirnov.musicplatform.service.implementation.SecurityContextServiceImpl;
import ru.smirnov.musicplatform.service.minio.MinioService;
import ru.smirnov.musicplatform.service.sql.relation.TrackByChartServiceOld;
import ru.smirnov.musicplatform.util.MinioPathUtil;
import ru.smirnov.musicplatform.util.SetUtil;
import ru.smirnov.musicplatform.validators.old.ChartValidator;
import ru.smirnov.musicplatform.validators.old.TrackValidator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChartServiceOld {

    private final ChartRepository chartRepository;
    private final MusicCollectionMapperOld musicCollectionMapper;

    private final SecurityContextServiceImpl securityContextService;

    private final MinioService minioService;
    private final MinioPathUtil minioPathUtil;

    private final TrackServiceOld trackService;
    private final TrackByChartServiceOld trackByChartService;

    private final ChartValidator chartValidator;
    private final TrackValidator trackValidator;

    private final AdminRepository adminRepository;

    @Autowired
    public ChartServiceOld(
            ChartRepository chartRepository,
            MusicCollectionMapperOld musicCollectionMapper,
            SecurityContextServiceImpl securityContextService,
            MinioService minioService,
            MinioPathUtil minioPathUtil,
            TrackServiceOld trackService,
            TrackByChartServiceOld trackByChartService,
            ChartValidator chartValidator,
            TrackValidator trackValidator,
            AdminRepository adminRepository
    ) {
        this.chartRepository = chartRepository;
        this.musicCollectionMapper = musicCollectionMapper;
        this.securityContextService = securityContextService;
        this.minioService = minioService;
        this.minioPathUtil = minioPathUtil;
        this.trackService = trackService;
        this.trackByChartService = trackByChartService;
        this.chartValidator = chartValidator;
        this.trackValidator = trackValidator;
        this.adminRepository = adminRepository;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ResponseEntity<Long> createChart(MusicCollectionToCreateDto dto) {

        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        Admin admin = this.adminRepository.findById(tokenData.getEntityId()).orElseThrow(
                () -> new ForbiddenException("Admin's business-data wasn't found by admin's id in token")
        );

        // [v] у чарта должно быть уникальное название среди других чартов
        this.chartValidator.checkChartExistenceByName(dto.getName());

        boolean coverAttached = (dto.getCover() != null && !dto.getCover().isEmpty());
        boolean tracksAttached = (dto.getTracks() != null && !dto.getTracks().isEmpty());

        Chart chart = this.chartRepository.save(this.musicCollectionMapper.musicCollectionToCreateDtoToChartEntity(dto, admin));

        if (coverAttached) {
            String coverReference = this.minioPathUtil.generateFormattedReference(MinioBuckets.CHART_COVER.getBucketName(), admin.getId(), chart.getId());
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    minioService.uploadObjectWithMetadata(
                            MinioBuckets.CHART_COVER.getBucketName(),
                            minioPathUtil.extractObjectName(coverReference),
                            dto.getCover(),
                            null
                    );
                }
            });

            chart.setImageReference(coverReference);
            this.chartRepository.save(chart);
        }

        if (tracksAttached) {
            // [v] каждый из треков должен существовать и должен быть PUBLIC
            List<Track> tracks = dto.getTracks().stream().map(trackId -> this.trackValidator.safelyGetByIdWithActiveStatus(trackId)).toList();
            this.trackByChartService.save(chart.getId(), tracks.stream().map(track -> track.getId()).toList());
        }

        return ResponseEntity.ok(chart.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ResponseEntity<Void> updateChart(MusicCollectionToUpdateDto dto) {
        // пока не уверен насчёт этой возможности, но как будто дать
        // администратору возможность редактировать любой чарт - нормально
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        Admin admin = this.adminRepository.findById(tokenData.getEntityId()).orElseThrow(
                () -> new ForbiddenException("Admin's business-data wasn't found by admin's id in token")
        );

        Chart chart = this.chartValidator.checkChartExistenceByNameForUpdate(dto.getName(), dto.getMusicCollectionId());

        boolean coverAttached = (dto.getCover() != null && !dto.getCover().isEmpty());
        boolean referenceExists = (chart.getImageReference() != null);

        chart.setName(dto.getName());
        chart.setDescription(dto.getDescription());

        if (coverAttached && referenceExists) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    minioService.replaceObjectInBucket(
                            MinioBuckets.CHART_COVER.getBucketName(),
                            minioPathUtil.extractObjectName(chart.getImageReference()),
                            dto.getCover()
                    );
                }
            });
        }

        if (!coverAttached && referenceExists) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    minioService.removeObject(
                            MinioBuckets.CHART_COVER.getBucketName(),
                            minioPathUtil.extractObjectName(chart.getImageReference())
                    );
                }
            });
            chart.setImageReference(null);
        }

        if (coverAttached && !referenceExists) {
            String coverReference = this.minioPathUtil.generateFormattedReference(MinioBuckets.CHART_COVER.getBucketName(), admin.getId(), chart.getId());
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    minioService.uploadObjectWithMetadata(
                            MinioBuckets.CHART_COVER.getBucketName(),
                            minioPathUtil.extractObjectName(coverReference),
                            dto.getCover(),
                            null
                    );
                }
            });
            chart.setImageReference(coverReference);
        }

        this.chartRepository.save(chart);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ResponseEntity<Void> updateChartAccessLevel(MusicCollectionAccessLevelUpdateDto dto) {
        // пока не уверен насчёт этой возможности, но как будто дать
        // администратору возможность редактировать любой чарт - нормально
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        Admin admin = this.adminRepository.findById(tokenData.getEntityId()).orElseThrow(
                () -> new ForbiddenException("Admin's business-data wasn't found by admin's id in token")
        );

        Chart chart = this.chartValidator.getChartByIdSafely(dto.getMusicCollectionId());

        chart.setAccessLevel(MusicCollectionAccessLevel.valueOf(dto.getMusicCollectionAccessLevel()));

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ResponseEntity<Void> updateChartByTracks(MusicCollectionTracksDto dto) {
        // пока не уверен насчёт этой возможности, но как будто дать
        // администратору возможность редактировать любой чарт - нормально
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        Admin admin = this.adminRepository.findById(tokenData.getEntityId()).orElseThrow(
                () -> new ForbiddenException("Admin's business-data wasn't found by admin's id in token")
        );

        // [v] проверка на то, что переданный чарт существует
        Chart chart = this.chartValidator.getChartByIdSafely(dto.getMusicCollectionId());

        // [v] проверка на то, что все переданные треки в принципе существуют и что они доступны
        for (Long trackId : dto.getTracks()) this.trackValidator.safelyGetByIdWithActiveStatus(trackId);

        Set<Long> tracksSetBeforeUpdate = chart.getTracks().stream().map(track -> track.getTrack().getId()).collect(Collectors.toSet());

        Set<Long> addedTracks = SetUtil.findAddedValues(tracksSetBeforeUpdate, dto.getTracks());
        Set<Long> removedTracks = SetUtil.findRemovedValues(tracksSetBeforeUpdate, dto.getTracks());

        this.trackByChartService.save(chart.getId(), addedTracks.stream().toList());
        this.trackByChartService.remove(chart.getId(), removedTracks.stream().toList());

//        CompletableFuture<ResponseEntity<MusicCollectionDataDto>> entityAfterCommit = new CompletableFuture<>();
//        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
//            @Override @SneakyThrows
//            public void afterCommit() {
//                ResponseEntity<MusicCollectionDataDto> response = getChartById(chart.getId());
//                entityAfterCommit.complete(response);
//            }
//        });
//
//        return entityAfterCommit.join();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    public ResponseEntity<MusicCollectionDataDto> getChartById(Long chartId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        Admin admin = this.adminRepository.findById(tokenData.getEntityId()).orElseThrow(
                () -> new ForbiddenException("Admin's business-data wasn't found by admin's id in token")
        );

        Chart chart = this.chartValidator.getChartByIdSafely(chartId);
        List<Long> tracks = chart.getTracks().stream()
                .map(track -> track.getTrack().getId())
                .toList();

        List<TrackShortcutDto> shortcuts = this.trackService.getTracksShortcutData(tracks, false);

        MusicCollectionDataDto dto = this.musicCollectionMapper.musicCollectionToMusicCollectionDataDto(
                chart,
                new MusicCollectionAuthorDto(admin.getId(), admin.getAccount().getUsername() + "[PLATFORM]"),
                shortcuts
        );

        return ResponseEntity.ok(dto);
    }

    @Transactional
    public ResponseEntity<MusicCollectionDataDto> getChartByIdSafely(Long chartId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        Admin admin = this.adminRepository.findById(tokenData.getEntityId()).orElseThrow(
                () -> new ForbiddenException("Admin's business-data wasn't found by admin's id in token")
        );

        Chart chart = this.chartValidator.getChartByIdSafely(chartId);
        chart.setNumberOfPlays(chart.getNumberOfPlays() + 1);
        this.chartRepository.save(chart);

        List<Long> tracks = chart.getTracks().stream()
                .map(track -> track.getTrack().getId())
                .toList();

        List<TrackShortcutDto> shortcuts = this.trackService.getTracksShortcutData(tracks, true);

        MusicCollectionDataDto dto = this.musicCollectionMapper.musicCollectionToMusicCollectionDataDto(
                chart,
                new MusicCollectionAuthorDto(admin.getId(), admin.getAccount().getUsername() + " [PLATFORM]"),
                shortcuts
        );

        return ResponseEntity.ok(dto);

    }
}
