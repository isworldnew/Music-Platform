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
import ru.smirnov.musicplatform.dto.domain.artist.ArtistRequest;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistResponse;
import ru.smirnov.musicplatform.dto.domain.artist.ExtendedArtistResponse;
import ru.smirnov.musicplatform.dto.domain.track.TrackRequest;
import ru.smirnov.musicplatform.service.abstraction.SecurityContextService;
import ru.smirnov.musicplatform.service.abstraction.domain.ArtistService;
import ru.smirnov.musicplatform.service.abstraction.domain.TrackService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final SecurityContextService securityContextService;

    private final ArtistService artistService;
    private final TrackService trackService;

    @Autowired
    public ArtistController(SecurityContextService securityContextService, ArtistService artistService, TrackService trackService) {
        this.securityContextService = securityContextService;
        this.artistService = artistService;
        this.trackService = trackService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public Long createArtist(@RequestBody @Valid ArtistRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.artistService.createArtist(dto, tokenData);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public void updateArtist(@NotNull @Positive @PathVariable("id") Long artistId, @RequestBody @Valid ArtistRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.artistService.updateArtist(artistId, dto, tokenData);
    }

    @GetMapping("/{id}/extended")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('DISTRIBUTOR', 'ADMIN')")
    public ExtendedArtistResponse getArtistWithDetails(@NotNull @Positive @PathVariable("id") Long artistId) {
        return this.artistService.getExtendedArtistDataById(artistId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ArtistResponse getArtist(@NotNull @Positive @PathVariable("id") Long artistId) {
        return this.artistService.getArtistDataById(artistId);
    }

    @PostMapping("/{id}/tracks")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public Long uploadTrack(@NotNull @Positive @PathVariable("id") Long artistId, @RequestBody @Valid TrackRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.trackService.uploadTrack(artistId, dto, tokenData);
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
