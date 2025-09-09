package ru.smirnov.musicplatform.controller.relation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.service.abstraction.relation.ArtistSocialNetworkService;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;

@RestController
@RequestMapping("/artists/social-networks")
public class ArtistSocialNetworkController {

    private final SecurityContextService securityContextService;
    private final ArtistSocialNetworkService artistSocialNetworkService;

    @Autowired
    public ArtistSocialNetworkController(
            SecurityContextService securityContextService,
            ArtistSocialNetworkService artistSocialNetworkService
    ) {
        this.securityContextService = securityContextService;
        this.artistSocialNetworkService = artistSocialNetworkService;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public void updateSocialNetwotkReference(
            @NotNull @Positive @PathVariable("id") Long socialNetworkId,
            @RequestParam(required = true) String reference
    ) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.artistSocialNetworkService.updateArtistSocialNetworkReference(socialNetworkId, reference, tokenData);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public void deleteSocialNetwork(@NotNull @Positive @PathVariable("id") Long socialNetworkId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.artistSocialNetworkService.deleteArtistSocialNetwork(socialNetworkId, tokenData);
    }
}
