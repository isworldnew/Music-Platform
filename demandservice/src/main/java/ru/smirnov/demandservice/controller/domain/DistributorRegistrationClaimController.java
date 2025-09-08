package ru.smirnov.demandservice.controller.domain;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.demandservice.dto.DistributorRegistrationClaimRequest;
import ru.smirnov.demandservice.service.abstraction.domain.DistributorRegistrationClaimService;
import ru.smirnov.demandservice.service.abstraction.security.SecurityContextService;

@RestController
@RequestMapping("/distributors/claims")
public class DistributorRegistrationClaimController {

    private final SecurityContextService service;
    private final DistributorRegistrationClaimService distributorRegistrationClaimService;

    @Autowired
    public DistributorRegistrationClaimController(SecurityContextService service, DistributorRegistrationClaimService distributorRegistrationClaimService) {
        this.service = service;
        this.distributorRegistrationClaimService = distributorRegistrationClaimService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long addDistributorRegistrationClaim(@RequestBody @Valid DistributorRegistrationClaimRequest dto) {
        return this.distributorRegistrationClaimService.addDistributorRegistrationClaim(dto);
    }

}
