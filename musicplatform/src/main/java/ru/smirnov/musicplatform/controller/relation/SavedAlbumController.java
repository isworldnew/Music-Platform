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
import ru.smirnov.musicplatform.service.abstraction.relation.SavedAlbumService;

@RestController
@RequestMapping("/saved-albums")
@Validated
public class SavedAlbumController {

    private final SecurityContextService securityContextService;
    private final SavedAlbumService savedAlbumService;

    @Autowired
    public SavedAlbumController(SecurityContextService securityContextService, SavedAlbumService savedAlbumService) {
        this.securityContextService = securityContextService;
        this.savedAlbumService = savedAlbumService;
    }

    @PostMapping("/{albumId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public Long addAlbum(@NotNull @Positive @PathVariable Long albumId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.savedAlbumService.addAlbum(albumId, tokenData);
    }

    @DeleteMapping("/{albumId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('USER')")
    public void removeAlbum(@NotNull @Positive @PathVariable Long albumId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.savedAlbumService.removeAlbum(albumId, tokenData);
    }

}
