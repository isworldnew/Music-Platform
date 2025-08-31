package ru.smirnov.musicplatform.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.musicplatform.dto.old.domain.track.TrackAccessLevelUpdateDto;
import ru.smirnov.musicplatform.dto.old.domain.track.TrackDataDto;
import ru.smirnov.musicplatform.dto.old.domain.track.TrackToCreateDto;
import ru.smirnov.musicplatform.projection.SavedTrackProjection;
import ru.smirnov.musicplatform.service.sql.domain.TrackServiceOld;
import ru.smirnov.musicplatform.service.sql.relation.SavedTrackServiceOld;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/tracks")
public class TrackController {

    private final TrackServiceOld trackService;
    private final SavedTrackServiceOld savedTrackService;

    @Autowired
    public TrackController(TrackServiceOld trackService, SavedTrackServiceOld savedTrackService) {
        this.trackService = trackService;
        this.savedTrackService = savedTrackService;
    }

    @PostMapping("/upload-track")
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public ResponseEntity<Long> uploadTrack(@Valid @ModelAttribute TrackToCreateDto dto) {
        return this.trackService.uploadTrack(dto);
    }

    @PatchMapping("/listen/{id}")
    public ResponseEntity<Void> listenTo(@NotNull @Positive @PathVariable("id") Long trackId) {
        return this.trackService.listenTo(trackId);
    }

    @PatchMapping("/set-access-level/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DISTRIBUTOR')")
    public ResponseEntity<TrackDataDto> setTrackAccessLevel(@NotNull @Positive @PathVariable Long id, @RequestBody @Valid TrackAccessLevelUpdateDto dto) {
        return this.trackService.setTrackAccessLevel(id, dto);
    }

    @GetMapping("/get-track-by-id-safely/{id}")
    public ResponseEntity<TrackDataDto> getTrackByIdSafely(@NotNull @Positive @PathVariable Long id) {
        return this.trackService.getTrackDataById(id, true);
    }

    @GetMapping("/get-track-by-id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DISTRIBUTOR')")
    public ResponseEntity<TrackDataDto> getTrackById(@NotNull @Positive @PathVariable Long id) {
        return this.trackService.getTrackDataById(id, false);
    }

    @PostMapping("/save/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Long> saveTrack(@NotNull @Positive @PathVariable Long id) {
        return this.savedTrackService.save(id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteTrack(@NotNull @Positive @PathVariable Long id) {
        return this.savedTrackService.delete(id);
    }

    @GetMapping("/saved")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<SavedTrackProjection>> getAllSavedTracks() {
        return this.savedTrackService.getAllSavedTracks();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().stream()
                .forEach(error -> {
                    String fieldName = ((FieldError)error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity.badRequest().body("Validation failed: " + ex.getMessage());
    }

}
