package ru.smirnov.demandservice.controller.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.demandservice.dto.TrackClaimResponse;
import ru.smirnov.demandservice.service.abstraction.domain.TrackClaimService;
import ru.smirnov.demandservice.service.abstraction.security.SecurityContextService;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.dtoregistry.dto.domain.TrackClaimRequest;

import java.util.List;

@RestController
@RequestMapping("/tracks/claims")
@Validated
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

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void processTrackClaim(
            @NotNull @Positive @PathVariable("id") Long claimId,
            @RequestBody @Valid TrackClaimRequest dto
    ) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.trackClaimService.processTrackClaim(claimId, dto, tokenData);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<TrackClaimResponse> getTrackClaims(@RequestParam(required = true) @NotNull Boolean relevantOnly) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.trackClaimService.getTrackClaims(relevantOnly, tokenData);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public TrackClaimResponse getTrackClaimById(@NotNull @Positive @PathVariable("id") Long claimId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.trackClaimService.getTrackClaimById(claimId, tokenData);
    }

}
