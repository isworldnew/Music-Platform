package ru.smirnov.musicplatform.controller.query;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionResponse;
import ru.smirnov.musicplatform.finder.abstraction.AlbumFinderService;
import ru.smirnov.musicplatform.projection.abstraction.MusicCollectionShortcutProjection;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;

import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumQueryController {

    private final SecurityContextService securityContextService;
    private final AlbumFinderService albumFinderService;

    @Autowired
    public AlbumQueryController(
            @Qualifier("anonymousSecurityContextServiceImplementation") SecurityContextService securityContextService,
            AlbumFinderService albumFinderRepository
    ) {
        this.securityContextService = securityContextService;
        this.albumFinderService = albumFinderRepository;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER', 'ANONYMOUS')")
    public List<MusicCollectionShortcutProjection> searchAlbums(
            @RequestParam(required = true) @NotBlank String searchRequest,
            @RequestParam(required = false) Boolean savedOnly
    ) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();

        boolean isAnonymous = tokenData.getAuthorities().stream().anyMatch(
                grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ANONYMOUS")
        );

        if (isAnonymous)
            return this.albumFinderService.searchAlbums(searchRequest, null, false);


        if (savedOnly == null)
            return this.albumFinderService.searchAlbums(searchRequest, tokenData.getEntityId(), false);

        return this.albumFinderService.searchAlbums(searchRequest, tokenData.getEntityId(), savedOnly);
    }

    @GetMapping("/saved")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER')")
    public List<MusicCollectionShortcutProjection> getSavedAlbums() {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.albumFinderService.getSavedAlbums(tokenData.getEntityId());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('DISTRIBUTOR', 'USER', 'ANONYMOUS')")
    public MusicCollectionResponse getAlbumById(@NotNull @Positive @PathVariable("id") Long albumId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.albumFinderService.getAlbumById(albumId, tokenData);
    }

}
