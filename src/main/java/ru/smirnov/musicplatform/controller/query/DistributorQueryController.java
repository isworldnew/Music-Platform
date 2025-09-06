package ru.smirnov.musicplatform.controller.query;

import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.musicplatform.dto.audience.distributor.DistributorShortcutResponse;
import ru.smirnov.musicplatform.finder.abstraction.DistributorFinderService;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;

import java.util.List;

@RestController
@RequestMapping("/distributors")
public class DistributorQueryController {

    private final SecurityContextService securityContextService;
    private final DistributorFinderService distributorFinderService;

    @Autowired
    public DistributorQueryController(SecurityContextService securityContextService, DistributorFinderService distributorFinderService) {
        this.securityContextService = securityContextService;
        this.distributorFinderService = distributorFinderService;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<DistributorShortcutResponse> searchDistributors(@RequestParam(required = true) @NotBlank String searchRequest) {
        return this.distributorFinderService.searchDistributors(searchRequest);
    }
}
