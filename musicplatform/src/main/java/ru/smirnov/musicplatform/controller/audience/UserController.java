package ru.smirnov.musicplatform.controller.audience;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.authentication.TokenGenerator;
import ru.smirnov.musicplatform.dto.audience.user.UserDataUpdateRequest;
import ru.smirnov.musicplatform.dto.audience.user.UserRegistrationRequest;
import ru.smirnov.musicplatform.dto.audience.user.UserResponse;
import ru.smirnov.musicplatform.dto.authentication.JwtResponse;
import ru.smirnov.dtoregistry.dto.authentication.LoginRequest;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.service.abstraction.audience.UserService;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final TokenGenerator tokenGenerator;
    private final SecurityContextService securityContextService;

    @Autowired
    public UserController(
            UserService userService,
            TokenGenerator tokenGenerator,
            SecurityContextService securityContextService
    ) {
        this.userService = userService;
        this.tokenGenerator = tokenGenerator;
        this.securityContextService = securityContextService;
    }

    @PostMapping("/registration")
    public ResponseEntity<JwtResponse> userRegistration(@RequestBody @Valid UserRegistrationRequest dto) {
        User user = this.userService.userRegistration(dto);
        return this.tokenGenerator.createTokens(
                new LoginRequest(dto.getAccountData().getUsername(), dto.getAccountData().getPassword())
        );
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('USER')")
    public void updateUserData(@RequestBody @Valid UserDataUpdateRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.userService.updateUserData(dto, tokenData);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER')")
    public UserResponse getUserData() {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.userService.getUserData(tokenData);
    }


    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().stream()
                .forEach(error -> {
                    String fieldName = ((FieldError)error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity.badRequest().body("Validation failed: " + ex.getMessage());
    }

}
