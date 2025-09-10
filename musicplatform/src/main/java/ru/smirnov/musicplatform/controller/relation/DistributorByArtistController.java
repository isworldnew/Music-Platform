package ru.smirnov.musicplatform.controller.relation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.relation.DistributorByArtistRelationRequest;
import ru.smirnov.musicplatform.service.abstraction.relation.DistributorByArtistService;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;

@RestController
@RequestMapping("/distributors-by-artists")
@Validated
public class DistributorByArtistController {

    private final SecurityContextService securityContextService;
    private final DistributorByArtistService distributorByArtistService;

    @Autowired
    public DistributorByArtistController(SecurityContextService securityContextService, DistributorByArtistService distributorByArtistService) {
        this.securityContextService = securityContextService;
        this.distributorByArtistService = distributorByArtistService;
    }

    @PatchMapping("/{distributorId}/{artistId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('DISTRIBUTOR', 'ADMIN')")
    public void updateRelationBetweenDistributorAndArtist(
            @NotNull @Positive @PathVariable Long distributorId,
            @NotNull @Positive @PathVariable Long artistId,
            @RequestBody @Valid DistributorByArtistRelationRequest dto
    ) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.distributorByArtistService.updateRelationBetweenDistributorAndArtist(distributorId, artistId, dto, tokenData);
    }
}
