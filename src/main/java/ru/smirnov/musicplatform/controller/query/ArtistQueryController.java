package ru.smirnov.musicplatform.controller.query;

import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistResponse;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistShortcutResponse;
import ru.smirnov.musicplatform.finder.abstraction.ArtistFinderService;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;

import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistQueryController {

    private final SecurityContextService securityContextService;
    private final ArtistFinderService artistFinderService;

    @Autowired
    public ArtistQueryController(
            @Qualifier("anonymousSecurityContextServiceImplementation") SecurityContextService securityContextService,
            ArtistFinderService artistFinderService
    ) {
        this.securityContextService = securityContextService;
        this.artistFinderService = artistFinderService;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ArtistResponse> searchArtists(@RequestParam(required = true) @NotBlank String searchRequest) {
        return this.artistFinderService.searchArtists(searchRequest);
    }
}
