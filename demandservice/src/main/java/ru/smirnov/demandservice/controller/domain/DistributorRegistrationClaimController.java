package ru.smirnov.demandservice.controller.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.demandservice.dto.DistributorRegistrationClaimRequest;
import ru.smirnov.demandservice.service.abstraction.domain.DistributorRegistrationClaimService;
import ru.smirnov.demandservice.service.abstraction.security.SecurityContextService;
import ru.smirnov.dtoregistry.dto.domain.DemandStatusRequest;

@RestController
@RequestMapping("/distributors/claims")
@Validated
public class DistributorRegistrationClaimController {

    private final SecurityContextService service;
    private final DistributorRegistrationClaimService distributorRegistrationClaimService;

    @Autowired
    public DistributorRegistrationClaimController(SecurityContextService service, DistributorRegistrationClaimService distributorRegistrationClaimService) {
        this.service = service;
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
        this.distributorRegistrationClaimService.processDistributorClaim(claimId, dto);
    }

}
