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
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionAccessLevelRequest;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionRequest;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;
import ru.smirnov.musicplatform.service.abstraction.domain.PlaylistService;

@RestController
@RequestMapping("/playlists")
@Validated
public class PlaylistController {

    private final SecurityContextService securityContextService;
    private final PlaylistService playlistService;

    @Autowired
    public PlaylistController(SecurityContextService securityContextService, PlaylistService playlistService) {
        this.securityContextService = securityContextService;
        this.playlistService = playlistService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public Long createPlaylist(@RequestBody @Valid MusicCollectionRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.playlistService.createPlaylist(dto, tokenData);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('USER')")
    public void updatePlaylist(@NotNull @Positive @PathVariable("id") Long playlistId, @RequestBody @Valid MusicCollectionRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.playlistService.updatePlaylist(playlistId, dto, tokenData);
    }

    @PatchMapping("/{id}/access-level")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('USER')")
    public void updatePlaylistAccessLevel(@NotNull @Positive @PathVariable("id") Long playlistId, @RequestBody @Valid MusicCollectionAccessLevelRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.playlistService.updatePlaylistAccessLevel(playlistId, dto, tokenData);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('USER')")
    public void deletePlaylist(@NotNull @Positive @PathVariable("id") Long playlistId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.playlistService.deletePlaylist(playlistId, tokenData);
    }
}
