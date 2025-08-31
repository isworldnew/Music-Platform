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
import ru.smirnov.musicplatform.dto.FileToUpdateDto;
import ru.smirnov.musicplatform.dto.old.domain.artist.ArtistDataDto;
import ru.smirnov.musicplatform.dto.old.domain.artist.ArtistExtendedDataDto;
import ru.smirnov.musicplatform.dto.old.domain.artist.ArtistToCreateDto;
import ru.smirnov.musicplatform.dto.old.domain.artist.ArtistToUpdateDto;
import ru.smirnov.musicplatform.service.sql.domain.ArtistServiceOld;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistServiceOld artistService;

    @Autowired
    public ArtistController(ArtistServiceOld artistService) {
        this.artistService = artistService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    // @Validated // вроде и без этого работало, но тут прикол в том, что тут связка @ModelAttribute и @Valid
    // а вот связка @RequestBody и @Valid без @Validated над контроллером или эндпоинтом - не сработает
    public ResponseEntity<Long> createArtist(@ModelAttribute @Valid ArtistToCreateDto dto) {
        return this.artistService.createArtist(dto);
    }

    @GetMapping("/artist-data-by-id/{id}")
    public ResponseEntity<ArtistDataDto> getArtistDataById(@NotNull @Positive @PathVariable Long id) {
        return this.artistService.getArtistDataById(id);
        // ПРОВЕРКА НА ТО, ЧТО У ИСПОЛНИТЕЛЯ НЕТ АКТИВНЫХ СВЯЗЕЙ И ВСЁ ВОТ ЭТО
    }

    @GetMapping("/artist-extended-data-by-id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DISTRIBUTOR')")
    public ResponseEntity<ArtistExtendedDataDto> getArtistExtendedDataById(@NotNull @Positive @PathVariable Long id) {
        return this.artistService.getArtistExtendedDataById(id);
    }

    @PatchMapping("/update-artist/{id}")
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public ResponseEntity<ArtistExtendedDataDto> updateArtistBasicData(@NotNull @Positive @PathVariable("id") Long artistId, @Valid @RequestBody ArtistToUpdateDto dto) {
        return this.artistService.updateArtistBasicData(artistId, dto);
    }

    @PatchMapping("/update-cover/{id}")
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public ResponseEntity<ArtistExtendedDataDto> updateArtistCover(@NotNull @Positive @PathVariable("id") Long artistId, @Valid @ModelAttribute FileToUpdateDto dto) {
        return this.artistService.updateArtistCover(artistId, dto);
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
