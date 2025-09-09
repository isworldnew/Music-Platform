package ru.smirnov.musicplatform.controller.relation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;
import ru.smirnov.musicplatform.service.abstraction.relation.TrackByChartService;

@RestController
@RequestMapping("/tracks-by-charts")
@Validated
public class TrackByChartController {

    private final SecurityContextService securityContextService;
    private final TrackByChartService trackByChartService;

    @Autowired
    public TrackByChartController(SecurityContextService securityContextService, TrackByChartService trackByChartService) {
        this.securityContextService = securityContextService;
        this.trackByChartService = trackByChartService;
    }

    @PostMapping("/{chartId}/{trackId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public Long addTrack(@NotNull @Positive @PathVariable Long chartId, @NotNull @Positive @PathVariable Long trackId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.trackByChartService.addTrack(chartId, trackId, tokenData);
    }

    @DeleteMapping("/{chartId}/{trackId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public void removeTrack(@NotNull @Positive @PathVariable Long chartId, @NotNull @Positive @PathVariable Long trackId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.trackByChartService.removeTrack(chartId, trackId, tokenData);
    }
}
