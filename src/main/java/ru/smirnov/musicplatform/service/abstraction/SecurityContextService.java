package ru.smirnov.musicplatform.service.abstraction;

import ru.smirnov.musicplatform.authentication.DataForToken;

public interface SecurityContextService {

    DataForToken safelyExtractTokenDataFromSecurityContext();

}
