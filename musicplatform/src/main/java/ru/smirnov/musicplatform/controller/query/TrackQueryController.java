package ru.smirnov.musicplatform.controller.query;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.track.TrackExtendedResponse;
import ru.smirnov.musicplatform.dto.domain.track.TrackResponse;
import ru.smirnov.musicplatform.finder.abstraction.TrackFinderService;
import ru.smirnov.musicplatform.precondition.abstraction.domain.TrackPreconditionService;
import ru.smirnov.musicplatform.projection.abstraction.TrackShortcutProjection;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/tracks")
public class TrackQueryController {
    
    private final SecurityContextService securityContextService;
    private final TrackFinderService trackFinderService;
    private final TrackPreconditionService trackPreconditionService;

    @Autowired
    public TrackQueryController(
            @Qualifier("anonymousSecurityContextServiceImplementation") SecurityContextService securityContextService,
            TrackFinderService trackFinderService,
            TrackPreconditionService trackPreconditionService
    ) {
        this.securityContextService = securityContextService;
        this.trackFinderService = trackFinderService;
        this.trackPreconditionService = trackPreconditionService;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER', 'ANONYMOUS')")
    public List<TrackShortcutProjection> searchTracks(
            @RequestParam(required = true) @NotBlank String searchRequest,
            @RequestParam(required = false) Boolean savedOnly
    ) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();

        boolean isAnonymous = tokenData.getAuthorities().stream().anyMatch(
                grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ANONYMOUS")
        );

        if (isAnonymous)
            return this.trackFinderService.searchTracks(searchRequest, null, false);


        if (savedOnly == null)
            return this.trackFinderService.searchTracks(searchRequest, tokenData.getEntityId(), false);

        return this.trackFinderService.searchTracks(searchRequest, tokenData.getEntityId(), savedOnly);
    }

    @GetMapping("/search/tagged-with")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER')")
    public List<TrackShortcutProjection> searchTracksByTagsCombination(@NotNull @NotEmpty @RequestParam(value = "tags", required = true) Set<Long> tagsId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.trackFinderService.searchTracksByTagsCombination(tokenData.getEntityId(), tagsId);
    }

    @GetMapping("/saved")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER')")
    public List<TrackShortcutProjection> getSavedTracks() {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.trackFinderService.getSavedTracks(tokenData.getEntityId());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ANONYMOUS', 'ADMIN', 'DISTRIBUTOR')")
    public TrackResponse getTrackData(@NotNull @Positive @PathVariable("id") Long trackId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.trackFinderService.getTrackData(trackId, tokenData);
    }

    @GetMapping("/{id}/extended")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER')")
    public TrackExtendedResponse getTrackExtendedData(@NotNull @Positive @PathVariable("id") Long trackId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.trackFinderService.getTrackExtendedData(trackId, tokenData);
    }

    @GetMapping("/search/global")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<TrackShortcutProjection> searchTracksGlobally(@RequestParam(required = true) @NotBlank String searchRequest) {
        return this.trackFinderService.searchTracksGloballyAdmin(searchRequest);
    }

    @GetMapping("{id}/existence")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void trackExistenceById(@NotNull @Positive @PathVariable("id") Long trackId) {
        this.trackPreconditionService.getByIdIfExists(trackId);
    }
}
