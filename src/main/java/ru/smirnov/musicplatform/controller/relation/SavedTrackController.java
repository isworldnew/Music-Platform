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
import ru.smirnov.musicplatform.service.abstraction.relation.SavedTrackService;

@RestController
@RequestMapping("/saved-tracks")
@Validated
public class SavedTrackController {

    private final SecurityContextService securityContextService;
    private final SavedTrackService savedTrackService;

    @Autowired
    public SavedTrackController(SecurityContextService securityContextService, SavedTrackService savedTrackService) {
        this.securityContextService = securityContextService;
        this.savedTrackService = savedTrackService;
    }

    @PostMapping("/{trackId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public Long addTrack(@NotNull @Positive @PathVariable Long trackId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.savedTrackService.saveTrack(trackId, tokenData);
    }

    @DeleteMapping("/{trackId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('USER')")
    public void removeTrack(@NotNull @Positive @PathVariable Long trackId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.savedTrackService.deleteTrackFromSaved(trackId, tokenData);
    }

}
