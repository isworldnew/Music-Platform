package ru.smirnov.musicplatform.controller.audience;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.audience.admin.AdminRequest;
import ru.smirnov.musicplatform.dto.audience.admin.AdminResponse;
import ru.smirnov.musicplatform.service.abstraction.audience.AdminService;
import ru.smirnov.musicplatform.service.abstraction.audience.DistributorService;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;

import java.util.List;

@RestController
@RequestMapping("/admins")
public class AdminController {

    private final SecurityContextService securityContextService;
    private final AdminService adminService;

    @Autowired
    public AdminController(SecurityContextService securityContextService, AdminService adminService) {
        this.securityContextService = securityContextService;
        this.adminService = adminService;
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void updateAdminData(@RequestBody @Valid AdminRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.adminService.updateAdminData(dto, tokenData);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public AdminResponse getAdminData() {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.adminService.getAdminData(tokenData);
    }

    @GetMapping("/enabled")
    @ResponseStatus(HttpStatus.OK)
    public List<Long> getAllEnabledAdmins() {
        return this.adminService.getAllEnabledAdmins();
    }

}
