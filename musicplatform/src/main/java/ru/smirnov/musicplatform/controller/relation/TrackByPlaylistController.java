package ru.smirnov.musicplatform.controller.relation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;
import ru.smirnov.musicplatform.service.abstraction.relation.TrackByPlaylistService;

@RestController
@RequestMapping("/tracks-by-playlists")
@Validated
public class TrackByPlaylistController {

    private final SecurityContextService securityContextService;
    private final TrackByPlaylistService trackByPlaylistService;

    @Autowired
    public TrackByPlaylistController(SecurityContextService securityContextService, TrackByPlaylistService trackByPlaylistService) {
        this.securityContextService = securityContextService;
        this.trackByPlaylistService = trackByPlaylistService;
    }

    @PostMapping("/{playlistId}/{trackId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public Long addTrack(@NotNull @Positive @PathVariable Long playlistId, @NotNull @Positive @PathVariable Long trackId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.trackByPlaylistService.addTrack(playlistId, trackId, tokenData);
    }

    @DeleteMapping("/{playlistId}/{trackId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public void removeTrack(@NotNull @Positive @PathVariable Long playlistId, @NotNull @Positive @PathVariable Long trackId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.trackByPlaylistService.removeTrack(playlistId, trackId, tokenData);
    }
}