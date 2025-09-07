package ru.smirnov.musicplatform.service.abstraction.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import ru.smirnov.dtoregistry.AuthenticationResponse;
import ru.smirnov.dtoregistry.OuterTokenRequest;

public interface OuterTokenValidatorService {

    AuthenticationResponse validateOuterToken(String jwtToken);
}
