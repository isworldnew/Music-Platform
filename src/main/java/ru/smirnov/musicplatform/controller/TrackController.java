package ru.smirnov.musicplatform.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.simpleframework.xml.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.musicplatform.dto.domain.track.TrackAccessLevelUpdateDto;
import ru.smirnov.musicplatform.dto.domain.track.TrackToCreateDto;
import ru.smirnov.musicplatform.service.sql.domain.TrackService;

import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/tracks")
public class TrackController {

    private final TrackService trackService;

    @Autowired
    public TrackController(TrackService trackService) {
        this.trackService = trackService;
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
    public ResponseEntity<Void> setTrackAccessLevel(@NotNull @Positive @PathVariable Long id, @RequestBody @Valid TrackAccessLevelUpdateDto dto) {
        return this.trackService.setTrackAccessLevel(id, dto);
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
