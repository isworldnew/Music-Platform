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
import ru.smirnov.musicplatform.dto.domain.artist.ArtistDataDto;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistExtendedData;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistToCreateDto;
import ru.smirnov.musicplatform.service.sql.domain.ArtistService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistService artistService;

    @Autowired
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public ResponseEntity<Long> createArtist(@ModelAttribute @Valid ArtistToCreateDto dto) {
        return this.artistService.createArtist(dto);
    }

    @GetMapping("/artist-data-by-id/{id}")
    public ResponseEntity<ArtistDataDto> getArtistDataById(@NotNull @Positive @PathVariable Long id) {
        return this.artistService.getArtistDataById(id);
    }

//    @GetMapping("/artist-extended-data-by-id/{id}")
//    @PreAuthorize("hasRole('DISTRIBUTOR')")
//    public ResponseEntity<ArtistExtendedData> getArtistExtendedDataById(@NotNull @Positive @PathVariable Long id) {
//
//    }

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
