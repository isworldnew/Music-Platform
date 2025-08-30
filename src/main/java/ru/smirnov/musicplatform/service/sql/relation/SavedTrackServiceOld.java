package ru.smirnov.musicplatform.service.sql.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.exception.BadRequestException;
import ru.smirnov.musicplatform.projection.SavedTrackProjection;
import ru.smirnov.musicplatform.repository.relation.SavedTrackRepository;
import ru.smirnov.musicplatform.service.implementation.SecurityContextServiceImpl;
import ru.smirnov.musicplatform.validators.old.TrackValidator;

import java.util.List;

@Service
public class SavedTrackServiceOld {

    private final SavedTrackRepository savedTrackRepository;

    private final SecurityContextServiceImpl securityContextService;

    private final TrackValidator trackValidator;

    @Autowired
    public SavedTrackServiceOld(
            SavedTrackRepository savedTrackRepository,
            SecurityContextServiceImpl securityContextService,
            TrackValidator trackValidator
    ) {
        this.savedTrackRepository = savedTrackRepository;
        this.securityContextService = securityContextService;
        this.trackValidator = trackValidator;
    }

    @Transactional
    public ResponseEntity<Long> save(Long trackId) {

        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();

        // проверка на то, что трек существует и то, что его можно сохранить
        this.trackValidator.safelyGetByIdWithActiveStatus(trackId);

        if (this.savedTrackRepository.trackHasBeenSavedAlready(tokenData.getEntityId(), trackId))
            throw new BadRequestException("Track has already been saved");


        Long relationId = this.savedTrackRepository.save(tokenData.getEntityId(), trackId);

        return ResponseEntity.status(HttpStatus.CREATED).body(relationId);
    }

    @Transactional
    public ResponseEntity<Void> delete(Long trackId) {

        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();

        if (this.savedTrackRepository.trackHasBeenSavedAlready(tokenData.getEntityId(), trackId))
            this.savedTrackRepository.deleteByTrackIdAndUserId(tokenData.getEntityId(), trackId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public ResponseEntity<List<SavedTrackProjection>> getAllSavedTracks() {

        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();

        return ResponseEntity.ok(
                this.savedTrackRepository.savedTracksByUserId(tokenData.getEntityId())
        );

    }

}
