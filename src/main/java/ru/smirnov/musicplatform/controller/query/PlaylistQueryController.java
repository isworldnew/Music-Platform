package ru.smirnov.musicplatform.controller.query;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionResponse;
import ru.smirnov.musicplatform.finder.abstraction.PlaylistFinderService;
import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;

import java.util.List;

@RestController
@RequestMapping("/playlists")
public class PlaylistQueryController {

    private final SecurityContextService securityContextService;
    private final PlaylistFinderService playlistFinderService;

    @Autowired
    public PlaylistQueryController(
            @Qualifier("anonymousSecurityContextServiceImplementation") SecurityContextService securityContextService,
            PlaylistFinderService playlistFinderService
    ) {
        this.securityContextService = securityContextService;
        this.playlistFinderService = playlistFinderService;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER', 'ANONYMOUS')")
    public List<MusicCollectionShortcutProjection> searchPlaylists(
            @RequestParam(required = true) @NotBlank String searchRequest,
            @RequestParam(required = false) Boolean savedOnly
    ) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();

        boolean isAnonymous = tokenData.getAuthorities().stream().anyMatch(
                grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ANONYMOUS")
        );

        if (isAnonymous)
            return this.playlistFinderService.searchPlaylists(searchRequest, null, false);


        if (savedOnly == null)
            return this.playlistFinderService.searchPlaylists(searchRequest, tokenData.getEntityId(), false);

        return this.playlistFinderService.searchPlaylists(searchRequest, tokenData.getEntityId(), savedOnly);
    }

    @GetMapping("/owned")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER')")
    public List<MusicCollectionShortcutProjection> getOwnedPlaylists() {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.playlistFinderService.getOwnedPlaylists(tokenData.getEntityId());
    }

    @GetMapping("/saved")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER')")
    public List<MusicCollectionShortcutProjection> getSavedPlaylists() {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.playlistFinderService.getSavedPlaylists(tokenData.getEntityId());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER', 'ANONYMOUS')")
    public MusicCollectionResponse getPlaylistById(@NotNull @Positive @PathVariable("id") Long playlistId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.playlistFinderService.getPlaylistById(playlistId, tokenData);
    }
}
