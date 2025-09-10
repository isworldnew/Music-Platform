package ru.smirnov.demandservice.controller.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.demandservice.dto.DistributorRegistrationClaimRequest;
import ru.smirnov.demandservice.dto.DistributorRegistrationClaimResponse;
import ru.smirnov.demandservice.dto.DistributorRegistrationClaimShortcutResponse;
import ru.smirnov.demandservice.service.abstraction.domain.DistributorRegistrationClaimService;
import ru.smirnov.demandservice.service.abstraction.security.SecurityContextService;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.dtoregistry.dto.domain.DemandStatusRequest;

import java.util.List;

@RestController
@RequestMapping("/distributors/claims")
@Validated
public class DistributorRegistrationClaimController {

    private final SecurityContextService securityContextService;
    private final DistributorRegistrationClaimService distributorRegistrationClaimService;

    @Autowired
    public DistributorRegistrationClaimController(SecurityContextService securityContextService, DistributorRegistrationClaimService distributorRegistrationClaimService) {
        this.securityContextService = securityContextService;
        this.distributorRegistrationClaimService = distributorRegistrationClaimService;
    }

    // [v]
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long addDistributorRegistrationClaim(@RequestBody @Valid DistributorRegistrationClaimRequest dto) {
        return this.distributorRegistrationClaimService.addDistributorRegistrationClaim(dto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void processDistributorClaim(
            @NotNull @Positive @PathVariable("id") Long claimId,
            @RequestBody @Valid DemandStatusRequest dto
    ) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.distributorRegistrationClaimService.processDistributorClaim(claimId, dto, tokenData);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<DistributorRegistrationClaimShortcutResponse> getDistributorRegistrationClaims(@RequestParam(required = true) @NotNull Boolean relevantOnly) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.distributorRegistrationClaimService.getDistributorRegistrationClaims(relevantOnly, tokenData);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public DistributorRegistrationClaimResponse getDistributorRegistrationClaimById(@NotNull @Positive @PathVariable("id") Long claimId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.distributorRegistrationClaimService.getDistributorRegistrationClaimById(claimId, tokenData);
    }

}
