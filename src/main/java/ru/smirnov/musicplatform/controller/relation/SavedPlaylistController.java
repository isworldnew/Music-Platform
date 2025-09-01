package ru.smirnov.musicplatform.controller.relation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.service.abstraction.SecurityContextService;
import ru.smirnov.musicplatform.service.abstraction.relation.SavedPlaylistService;

@RestController
@RequestMapping("/saved-playlists")
@Validated
public class SavedPlaylistController {

    private final SecurityContextService securityContextService;
    private final SavedPlaylistService savedPlaylistService;

    @Autowired
    public SavedPlaylistController(SecurityContextService securityContextService, SavedPlaylistService savedPlaylistService) {
        this.securityContextService = securityContextService;
        this.savedPlaylistService = savedPlaylistService;
    }

    @PostMapping("/{playlistId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public Long addPlaylist(@NotNull @Positive @PathVariable Long playlistId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.savedPlaylistService.addPlaylist(playlistId, tokenData);
    }

    @DeleteMapping("/{playlistId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('USER')")
    public void removePlaylist(@NotNull @Positive @PathVariable Long playlistId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.savedPlaylistService.removePlaylist(playlistId, tokenData);
    }

}