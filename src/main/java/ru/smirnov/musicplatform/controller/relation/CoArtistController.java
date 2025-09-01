package ru.smirnov.musicplatform.controller.relation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.relation.CoArtistRequest;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;
import ru.smirnov.musicplatform.service.abstraction.relation.CoArtistService;

@RestController
@RequestMapping("/co-artists")
@Validated
public class CoArtistController {

    private final SecurityContextService securityContextService;
    private final CoArtistService coArtistService;

    @Autowired
    public CoArtistController(SecurityContextService securityContextService, CoArtistService coArtistService) {
        this.securityContextService = securityContextService;
        this.coArtistService = coArtistService;
    }

    @PostMapping("/{trackId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public Long addCoArtistToTrack(@NotNull @Positive @PathVariable Long trackId, @RequestBody @Valid CoArtistRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.coArtistService.addCoArtistToTrack(trackId, dto, tokenData);
    }

    @DeleteMapping("/{trackId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public void removeCoArtistFromTrack(@NotNull @Positive @PathVariable Long trackId, @RequestBody @Valid CoArtistRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.coArtistService.removeCoArtistFromTrack(trackId, dto, tokenData);
    }
}
