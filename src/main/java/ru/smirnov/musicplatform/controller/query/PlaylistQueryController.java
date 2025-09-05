package ru.smirnov.musicplatform.controller.query;

import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.musicplatform.authentication.DataForToken;
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

}
