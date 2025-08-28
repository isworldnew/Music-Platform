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
import ru.smirnov.musicplatform.service.sql.domain.PlaylistService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {
    
    private final PlaylistService playlistService;

    @Autowired
    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Long> createPlaylist(@Valid @ModelAttribute MusicCollectionToCreateDto dto) {
        return this.playlistService.createPlaylist(dto);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> updatePlaylist(@Valid @ModelAttribute MusicCollectionToUpdateDto dto) {
        return this.playlistService.updatePlaylist(dto);
    }

    @PatchMapping("/update-access-level")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> updateAccessLevel(@Valid @RequestBody MusicCollectionAccessLevelUpdateDto dto) {
        return this.playlistService.updatePlaylistAccessLevel(dto);
    }

    @PatchMapping("/update-by-tracks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> updatePlaylistByTracks(@Valid @RequestBody MusicCollectionTracksDto dto) {
        return this.playlistService.updatePlaylistByTracks(dto);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<MusicCollectionDataDto> getPlaylistById(@NotNull @Positive @PathVariable("id") Long chartId) {
        return this.playlistService.getPlaylistById(chartId);
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
