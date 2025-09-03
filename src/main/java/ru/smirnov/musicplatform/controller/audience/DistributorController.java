package ru.smirnov.musicplatform.controller.audience;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.audience.distributor.DistributorRequest;
import ru.smirnov.musicplatform.dto.audience.distributor.DistributorResponse;
import ru.smirnov.musicplatform.service.abstraction.audience.DistributorService;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;

@RestController
@RequestMapping("/distributors")
public class DistributorController {

    private final SecurityContextService securityContextService;
    private final DistributorService distributorService;

    @Autowired
    public DistributorController(
            SecurityContextService securityContextService,
            DistributorService distributorService
    ) {
        this.securityContextService = securityContextService;
        this.distributorService = distributorService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public DistributorResponse getDistributorData() {
        DataForToken tokeData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.distributorService.getDistributorData(tokeData);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public void updateDistributorData(@RequestBody @Valid DistributorRequest dto) {
        DataForToken tokeData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.distributorService.updateDistributorData(dto, tokeData);
    }

}
