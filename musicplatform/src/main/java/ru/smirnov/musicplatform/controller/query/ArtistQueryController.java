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
import ru.smirnov.musicplatform.dto.domain.artist.ArtistExtendedResponse;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistResponse;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistShortcutResponse;
import ru.smirnov.musicplatform.finder.abstraction.ArtistFinderService;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;

import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistQueryController {

    private final SecurityContextService securityContextService;
    private final ArtistFinderService artistFinderService;

    @Autowired
    public ArtistQueryController(
            @Qualifier("anonymousSecurityContextServiceImplementation") SecurityContextService securityContextService,
            ArtistFinderService artistFinderService
    ) {
        this.securityContextService = securityContextService;
        this.artistFinderService = artistFinderService;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ArtistShortcutResponse> searchArtists(@RequestParam(required = true) @NotBlank String searchRequest) {
        return this.artistFinderService.searchArtists(searchRequest);
    }

    @GetMapping("/search/distributed")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public List<ArtistShortcutResponse> searchArtistsByDistributor(
            @RequestParam(required = true) @NotBlank String searchRequest

    ) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.artistFinderService.searchArtists(searchRequest, tokenData.getEntityId());
    }

    @GetMapping("/distributed")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public List<ArtistShortcutResponse> getDistributedArtists(
            @RequestParam(required = true) @NotNull Boolean activelyDistributed
    ) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.artistFinderService.getDistributedArtists(tokenData.getEntityId(), activelyDistributed);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER', 'ANONYMOUS')")
    public ArtistResponse getArtistData(@NotNull @Positive @PathVariable("id") Long artistId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.artistFinderService.getArtistData(artistId, tokenData);
    }

    @GetMapping("/{id}/extended")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'DISTRIBUTOR')")
    public ArtistExtendedResponse getArtistExtendedData(@NotNull @Positive @PathVariable("id") Long artistId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.artistFinderService.getArtistExtendedData(artistId, tokenData);
    }
}
