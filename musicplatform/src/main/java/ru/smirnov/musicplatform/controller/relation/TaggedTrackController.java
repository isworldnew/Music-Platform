package ru.smirnov.musicplatform.controller.relation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.service.abstraction.relation.TaggedTrackService;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;

@RestController
@RequestMapping("/tagged-tracks")
public class TaggedTrackController {

    private final SecurityContextService securityContextService;
    private final TaggedTrackService taggedTrackService;

    @Autowired
    public TaggedTrackController(SecurityContextService securityContextService, TaggedTrackService taggedTrackService) {
        this.securityContextService = securityContextService;
        this.taggedTrackService = taggedTrackService;
    }

    @PostMapping("/{tagId}/{trackId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public Long tagTrack(@NotNull @Positive @PathVariable Long tagId, @NotNull @Positive @PathVariable Long trackId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.taggedTrackService.tagTrack(trackId, tagId, tokenData);
    }

    @DeleteMapping("/{tagId}/{trackId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('USER')")
    public void unTagTrack(@NotNull @Positive @PathVariable Long tagId, @NotNull @Positive @PathVariable Long trackId) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.taggedTrackService.unTagTrack(trackId, tagId, tokenData);
    }

}
