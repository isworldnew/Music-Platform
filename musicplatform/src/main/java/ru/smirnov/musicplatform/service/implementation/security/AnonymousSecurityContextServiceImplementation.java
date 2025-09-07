package ru.smirnov.musicplatform.service.implementation.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.exception.SecurityContextException;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;

import java.util.List;


@Service("anonymousSecurityContextServiceImplementation")
public class AnonymousSecurityContextServiceImplementation implements SecurityContextService {

    @Override
    public DataForToken safelyExtractTokenDataFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAnonymous = authentication.getAuthorities().stream().anyMatch(
                grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ANONYMOUS")
        );

        if (isAnonymous) return DataForToken.builder().authorities(List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))).build();

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof DataForToken))
            throw new SecurityContextException("Principal in authentication is not instance of DataForToken");

        return (DataForToken) principal;
    }
}
