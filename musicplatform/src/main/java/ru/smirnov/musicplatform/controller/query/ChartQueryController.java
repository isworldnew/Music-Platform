package ru.smirnov.musicplatform.controller.query;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionResponse;
import ru.smirnov.musicplatform.finder.abstraction.ChartFinderService;
import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;

import java.util.List;

@RestController
@RequestMapping("/charts")
public class ChartQueryController {

    private final SecurityContextService securityContextService;
    private final ChartFinderService chartFinderService;

    @Autowired
    public ChartQueryController(
            @Qualifier("anonymousSecurityContextServiceImplementation") SecurityContextService securityContextService,
            ChartFinderService chartFinderService
    ) {
        this.securityContextService = securityContextService;
        this.chartFinderService = chartFinderService;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER', 'ANONYMOUS')")
    public List<MusicCollectionShortcutProjection> searchCharts(
            @RequestParam(required = true) @NotBlank String searchRequest,
            @RequestParam(required = false) Boolean savedOnly
    ) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();

        boolean isAnonymous = tokenData.getAuthorities().stream().anyMatch(
                grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ANONYMOUS")
        );

        if (isAnonymous)
            return this.chartFinderService.searchCharts(searchRequest, null, false);


        if (savedOnly == null)
            return this.chartFinderService.searchCharts(searchRequest, tokenData.getEntityId(), false);

        return this.chartFinderService.searchCharts(searchRequest, tokenData.getEntityId(), savedOnly);
    }

    @GetMapping("/search/by-admin")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<MusicCollectionShortcutProjection> searchChartsByAdmin(@RequestParam(required = true) @NotBlank String searchRequest) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.chartFinderService.searchChartsByAdmin(searchRequest, tokenData.getEntityId());
    }

    @GetMapping("/saved")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER')")
    public List<MusicCollectionShortcutProjection> getSavedCharts() {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.chartFinderService.getSavedCharts(tokenData.getEntityId());
    }

    @GetMapping("/search/admin-rights")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<MusicCollectionShortcutProjection> searchChartsGloballyAdmin(@RequestParam(required = true) @NotBlank String searchRequest) {
        return this.chartFinderService.searchChartsGloballyAdmin(searchRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'ANONYMOUS')")
    public MusicCollectionResponse getChartById(@NotNull @Positive @PathVariable("id") Long chartId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.chartFinderService.getChartById(chartId, tokenData);
    }
}
