package ru.smirnov.musicplatform.controller.relation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;
import ru.smirnov.musicplatform.service.abstraction.relation.TrackByAlbumService;

@RestController
@RequestMapping("/tracks-by-albums")
@Validated
public class TrackByAlbumController {

    private final SecurityContextService securityContextService;
    private final TrackByAlbumService trackByAlbumService;

    @Autowired
    public TrackByAlbumController(SecurityContextService securityContextService, TrackByAlbumService trackByAlbumService) {
        this.securityContextService = securityContextService;
        this.trackByAlbumService = trackByAlbumService;
    }

    @PostMapping("/{albumId}/{trackId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public Long addTrack(@NotNull @Positive @PathVariable Long albumId, @NotNull @Positive @PathVariable Long trackId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.trackByAlbumService.addTrack(albumId, trackId, tokenData);
    }

    @DeleteMapping("/{albumId}/{trackId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public void removeTrack(@NotNull @Positive @PathVariable Long albumId, @NotNull @Positive @PathVariable Long trackId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.trackByAlbumService.removeTrack(albumId, trackId, tokenData);
    }
}
