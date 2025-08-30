package ru.smirnov.musicplatform.service.interfaces;

import ru.smirnov.musicplatform.authentication.DataForToken;

public interface SecurityContextService {

    DataForToken safelyExtractTokenDataFromSecurityContext();

}
