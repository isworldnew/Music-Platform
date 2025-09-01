package ru.smirnov.musicplatform.controller.relation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.service.abstraction.SecurityContextService;
import ru.smirnov.musicplatform.service.abstraction.relation.SavedChartService;

@RestController
@RequestMapping("/saved-charts")
@Validated
public class SavedChartController {

    private final SecurityContextService securityContextService;
    private final SavedChartService savedChartService;

    @Autowired
    public SavedChartController(SecurityContextService securityContextService, SavedChartService savedChartService) {
        this.securityContextService = securityContextService;
        this.savedChartService = savedChartService;
    }

    @PostMapping("/{chartId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public Long addChart(@NotNull @Positive @PathVariable Long chartId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.savedChartService.addChart(chartId, tokenData);
    }

    @DeleteMapping("/{chartId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('USER')")
    public void removeChart(@NotNull @Positive @PathVariable Long chartId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.savedChartService.removeChart(chartId, tokenData);
    }

}