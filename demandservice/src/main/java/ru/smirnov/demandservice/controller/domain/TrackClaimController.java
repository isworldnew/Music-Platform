package ru.smirnov.demandservice.controller.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.demandservice.service.abstraction.domain.TrackClaimService;
import ru.smirnov.demandservice.service.abstraction.security.SecurityContextService;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;

@RestController
@RequestMapping("/tracks/claims")
public class TrackClaimController {

    private final SecurityContextService securityContextService;
    private final TrackClaimService trackClaimService;

    @Autowired
    public TrackClaimController(SecurityContextService securityContextService, TrackClaimService trackClaimService) {
        this.securityContextService = securityContextService;
        this.trackClaimService = trackClaimService;
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public Long addTrackClaim(@NotNull @Positive @PathVariable("id") Long trackId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.trackClaimService.addTrackClaim(trackId, tokenData);
    }
}
