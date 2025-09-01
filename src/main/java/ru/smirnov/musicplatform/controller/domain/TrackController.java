package ru.smirnov.musicplatform.controller.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.track.TrackAccessLevelRequest;
import ru.smirnov.musicplatform.dto.domain.track.TrackRequest;
import ru.smirnov.musicplatform.service.abstraction.SecurityContextService;
import ru.smirnov.musicplatform.service.abstraction.domain.TrackService;


@RestController
@RequestMapping("/tracks")
@Validated
public class TrackController {

    private final SecurityContextService securityContextService;
    private final TrackService trackService;

    @Autowired
    public TrackController(TrackService trackService, SecurityContextService securityContextService) {
        this.trackService = trackService;
        this.securityContextService = securityContextService;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public void updateTrack(@NotNull @Positive @PathVariable("id") Long trackId, @RequestBody @Valid TrackRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.trackService.updateTrack(trackId, dto, tokenData);
    }

    @PatchMapping("/{id}/access-level")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public void updateTrackAccessLevel(@NotNull @Positive @PathVariable("id") Long trackId, @RequestBody @Valid TrackAccessLevelRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.trackService.updateTrackAccessLevel(trackId, dto, tokenData);
    }

    @PatchMapping("/{id}/listen")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void listenToTrack(@NotNull @Positive @PathVariable("id") Long trackId) {
        this.trackService.listenToTrack(trackId);
    }

}
