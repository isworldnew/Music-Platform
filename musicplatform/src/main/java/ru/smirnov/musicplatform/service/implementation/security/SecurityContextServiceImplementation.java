package ru.smirnov.musicplatform.service.implementation.security;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.exception.SecurityContextException;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;

@Primary
@Service
public class SecurityContextServiceImplementation implements SecurityContextService {

    @Override
    public DataForToken safelyExtractTokenDataFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated())
            throw new SecurityContextException("Unable to set authentication from security context");

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof DataForToken))
            throw new SecurityContextException("Principal in authentication is not instance of DataForToken");

        return (DataForToken) principal;
    }

}
