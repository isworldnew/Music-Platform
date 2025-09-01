package ru.smirnov.musicplatform.service.abstraction.security;

import ru.smirnov.musicplatform.authentication.DataForToken;

public interface SecurityContextService {

    DataForToken safelyExtractTokenDataFromSecurityContext();

}
