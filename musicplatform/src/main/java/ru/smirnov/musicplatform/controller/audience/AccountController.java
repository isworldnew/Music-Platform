package ru.smirnov.musicplatform.controller.audience;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.authentication.LoginRequest;
import ru.smirnov.musicplatform.service.abstraction.audience.AccountService;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;

@RestController("/accounts")
public class AccountController {

    private final SecurityContextService securityContextService;
    private final AccountService accountService;

    @Autowired
    public AccountController(SecurityContextService securityContextService, AccountService accountService) {
        this.securityContextService = securityContextService;
        this.accountService = accountService;
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'DISTRIBUTOR', 'USER')")
    public void updateAccount(@Valid @RequestBody LoginRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.accountService.updateAccount(dto, tokenData);
    }

}
