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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.track.TrackAccessLevelRequest;
import ru.smirnov.musicplatform.dto.domain.track.TrackRequest;
import ru.smirnov.musicplatform.service.abstraction.SecurityContextService;
import ru.smirnov.musicplatform.service.abstraction.domain.TrackService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/tracks")
public class TrackController {

    private final TrackService trackService;
    private final SecurityContextService securityContextService;

    @Autowired
    public TrackController(TrackService trackService, SecurityContextService securityContextService) {
        this.trackService = trackService;
        this.securityContextService = securityContextService;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public void updateTrack(@NotNull @Positive @PathVariable("id") Long trackId, @RequestBody @Valid TrackRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.trackService.updateTrack(trackId, dto, tokenData);
    }

    @PatchMapping("/{id}/access-level")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public void updateTrackAccessLevel(@NotNull @Positive @PathVariable("id") Long trackId, @RequestBody @Valid TrackAccessLevelRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.trackService.updateTrackAccessLevel(trackId, dto, tokenData);
    }

    @PatchMapping("/{id}/listen")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void listenToTrack(@NotNull @Positive @PathVariable("id") Long trackId) {
        this.trackService.listenToTrack(trackId);
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
