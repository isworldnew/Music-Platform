package ru.smirnov.demandservice.service.abstraction.security;

import ru.smirnov.dtoregistry.dto.authentication.DataForToken;

public interface SecurityContextService {
    DataForToken safelyExtractTokenDataFromSecurityContext();
}
