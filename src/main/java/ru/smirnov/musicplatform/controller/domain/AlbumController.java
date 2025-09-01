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
import ru.smirnov.musicplatform.service.abstraction.domain.AlbumService;

@RestController
@RequestMapping("/albums")
@Validated
public class AlbumController {

    private final SecurityContextService securityContextService;
    private final AlbumService albumService;

    @Autowired
    public AlbumController(SecurityContextService securityContextService, AlbumService albumService) {
        this.securityContextService = securityContextService;
        this.albumService = albumService;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public void updateAlbum(@NotNull @Positive @PathVariable Long albumId, @RequestBody @Valid MusicCollectionRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.albumService.updateAlbum(albumId, dto, tokenData);
    }

    @PatchMapping("/{id}/access-level")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public void updateAlbumAccessLevel(@NotNull @Positive @PathVariable Long albumId, @RequestBody @Valid MusicCollectionAccessLevelRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.albumService.updateAlbumAccessLevel(albumId, dto, tokenData);
    }
}
