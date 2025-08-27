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
import ru.smirnov.musicplatform.dto.domain.album.*;
import ru.smirnov.musicplatform.service.sql.domain.AlbumService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    private final AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public ResponseEntity<Long> createAlbum(@Valid @ModelAttribute AlbumToCreateDto dto) {
        return this.albumService.createAlbum(dto);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public ResponseEntity<MusicCollectionDataDto> updateAlbum(@Valid @ModelAttribute MusicCollectionToUpdateDto dto) {
        return this.albumService.updateAlbum(dto);
    }

    @PatchMapping("/update-access-level")
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public ResponseEntity<MusicCollectionDataDto> updateAccessLevel(@Valid @RequestBody MusicCollectionAccessLevelUpdateDto dto) {
        return this.albumService.updateAccessLevel(dto);
    }

    @PatchMapping("/update-by-tracks")
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public ResponseEntity<MusicCollectionDataDto> updateAlbumByTracks(@Valid @RequestBody MusicCollectionTracksDto dto) {
        return this.albumService.updateAlbumByTracks(dto);
    }

    @GetMapping("/get-by-id/{id}")
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public ResponseEntity<MusicCollectionDataDto> getAlbumById(@NotNull @Positive @PathVariable("id") Long albumId) {
        return this.albumService.getAlbumById(albumId);
    }

    @GetMapping("/get-by-id-safely/{id}")
    public ResponseEntity<MusicCollectionDataDto> getAlbumByIdSafely(@NotNull @Positive @PathVariable("id") Long albumId) {
        return this.albumService.getAlbumByIdSafely(albumId);
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
