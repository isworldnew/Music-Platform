package ru.smirnov.musicplatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;

@RestController
@RequestMapping("/test")
public class TestController {

    private final SecurityContextService securityContextService;

    @Autowired
    public TestController(@Qualifier("anonymousSecurityContextServiceImplementation") SecurityContextService securityContextService) {
        this.securityContextService = securityContextService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ANONYMOUS')")
    public String test() {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();

        boolean isAnonymous = tokenData.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ANONYMOUS"));

        if (isAnonymous)
            return "ANONYMOUS";

        else return "USER (id=" + tokenData.getEntityId() + ")";
    }

}
