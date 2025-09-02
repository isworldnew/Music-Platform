package ru.smirnov.musicplatform.controller.query;

import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionShortcutResponse;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.finder.SearchResult;
import ru.smirnov.musicplatform.finder.abstraction.AlbumFinderService;
import ru.smirnov.musicplatform.repository.audience.UserRepository;
import ru.smirnov.musicplatform.repository.domain.finder.AlbumFinderRepository;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;

import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumQueryController {

    private final SecurityContextService securityContextService;
    private final AlbumFinderService albumFinderService;
    private final UserRepository userRepository;

    @Autowired
    public AlbumQueryController(
            @Qualifier("anonymousSecurityContextServiceImplementation") SecurityContextService securityContextService,
            AlbumFinderService albumFinderRepository,
            UserRepository userRepository
    ) {
        this.securityContextService = securityContextService;
        this.albumFinderService = albumFinderRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER, ANONYMOUS')")
    public List<MusicCollectionShortcutResponse> searchAlbum(
            @RequestParam(required = true) @NotBlank String searchRequest,
            @RequestParam(required = false) Boolean savedOnly
    ) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();

        boolean isAnonymous = tokenData.getAuthorities().stream().anyMatch(
                grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ANONYMOUS")
        );

        if (isAnonymous)
            return this.albumFinderService.searchAlbums(searchRequest, null, false, SearchResult.GUEST);

        User user = this.userRepository.findById(tokenData.getEntityId()).orElseThrow(
                () -> new ForbiddenException("User's business-data wasn't found by users's id in token")
        );

        if (savedOnly == null || savedOnly == false)
            return this.albumFinderService.searchAlbums(searchRequest, user, false, SearchResult.USER);

        return this.albumFinderService.searchAlbums(searchRequest, user, true, SearchResult.USER_SAVED);
    }
}
