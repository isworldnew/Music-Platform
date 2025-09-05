package ru.smirnov.musicplatform.controller.query;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistResponse;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistShortcutResponse;
import ru.smirnov.musicplatform.finder.abstraction.ArtistFinderService;
import ru.smirnov.musicplatform.precondition.abstraction.audience.DistributorPreconditionService;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;

import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistQueryController {

    private final SecurityContextService securityContextService;
    private final ArtistFinderService artistFinderService;
    private final DistributorPreconditionService distributorPreconditionService;

    @Autowired
    public ArtistQueryController(
            @Qualifier("anonymousSecurityContextServiceImplementation") SecurityContextService securityContextService,
            ArtistFinderService artistFinderService,
            DistributorPreconditionService distributorPreconditionService
    ) {
        this.securityContextService = securityContextService;
        this.artistFinderService = artistFinderService;
        this.distributorPreconditionService = distributorPreconditionService;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ArtistResponse> searchArtists(@RequestParam(required = true) @NotBlank String searchRequest) {
        return this.artistFinderService.searchArtists(searchRequest);
    }

    @GetMapping("/search/distributors/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<ArtistResponse> searchArtistsByDistributor(
            @NotNull @Positive @PathVariable("id") Long distributorId,
            @RequestParam(required = true) @NotBlank String searchRequest

    ) {
        this.distributorPreconditionService.getByIdIfExists(distributorId);
        return this.artistFinderService.searchArtists(searchRequest, distributorId);
    }
}
