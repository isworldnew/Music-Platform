package ru.smirnov.musicplatform.controller.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionAccessLevelRequest;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionRequest;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;
import ru.smirnov.musicplatform.service.abstraction.domain.ChartService;

@RestController
@RequestMapping("/charts")
@Validated
public class ChartController {

    private final SecurityContextService securityContextService;
    private final ChartService chartService;

    @Autowired
    public ChartController(SecurityContextService securityContextService, ChartService chartService) {
        this.securityContextService = securityContextService;
        this.chartService = chartService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public Long createChart(@RequestBody @Valid MusicCollectionRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.chartService.createChart(dto, tokenData);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void updateChart(@NotNull @Positive @PathVariable("id") Long chartId, @RequestBody @Valid MusicCollectionRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.chartService.updateChart(chartId, dto, tokenData);
    }

    @PatchMapping("/{id}/access-level")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void updateChartAccessLevel(@NotNull @Positive @PathVariable("id") Long chartId, @RequestBody @Valid MusicCollectionAccessLevelRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.chartService.updateChartAccessLevel(chartId, dto, tokenData);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteChart(@NotNull @Positive @PathVariable("id") Long chartId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.chartService.deleteChart(chartId, tokenData);
    }

}
