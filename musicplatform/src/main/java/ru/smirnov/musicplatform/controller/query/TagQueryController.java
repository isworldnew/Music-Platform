package ru.smirnov.musicplatform.controller.query;

import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.tag.TagResponse;
import ru.smirnov.musicplatform.finder.abstraction.TagFinderService;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagQueryController {

    private final SecurityContextService securityContextService;
    private final TagFinderService tagFinderService;

    @Autowired
    public TagQueryController(SecurityContextService securityContextService, TagFinderService tagFinderService) {
        this.securityContextService = securityContextService;
        this.tagFinderService = tagFinderService;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER')")
    public List<TagResponse> searchTags(@RequestParam(required = true) @NotBlank String searchRequest) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.tagFinderService.searchTags(searchRequest, tokenData.getEntityId());
    }

}
