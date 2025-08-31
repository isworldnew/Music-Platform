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
import ru.smirnov.musicplatform.dto.old.domain.album.*;
import ru.smirnov.musicplatform.service.sql.domain.ChartServiceOld;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/charts")
public class ChartController {

    private final ChartServiceOld chartService;

    @Autowired
    public ChartController(ChartServiceOld chartService) {
        this.chartService = chartService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> createChart(@Valid @ModelAttribute MusicCollectionToCreateDto dto) {
        return this.chartService.createChart(dto);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateChart(@Valid @ModelAttribute MusicCollectionToUpdateDto dto) {
        return this.chartService.updateChart(dto);
    }

    @PatchMapping("/update-access-level")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateAccessLevel(@Valid @RequestBody MusicCollectionAccessLevelUpdateDto dto) {
        return this.chartService.updateChartAccessLevel(dto);
    }

    @PatchMapping("/update-by-tracks")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateChartByTracks(@Valid @RequestBody MusicCollectionTracksDto dto) {
        return this.chartService.updateChartByTracks(dto);
    }

    @GetMapping("/get-by-id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MusicCollectionDataDto> getChartById(@NotNull @Positive @PathVariable("id") Long chartId) {
        return this.chartService.getChartById(chartId);
    }

    @GetMapping("/get-by-id-safely/{id}")
    public ResponseEntity<MusicCollectionDataDto> getChartByIdSafely(@NotNull @Positive @PathVariable("id") Long chartId) {
        return this.chartService.getChartByIdSafely(chartId);
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
