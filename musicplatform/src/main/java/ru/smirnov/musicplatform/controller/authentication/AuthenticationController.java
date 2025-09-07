package ru.smirnov.musicplatform.controller.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.dtoregistry.AuthenticationResponse;
import ru.smirnov.dtoregistry.OuterTokenRequest;
import ru.smirnov.musicplatform.authentication.TokenGenerator;
import ru.smirnov.musicplatform.dto.authentication.LoginRequest;
import ru.smirnov.musicplatform.dto.authentication.JwtResponse;
import ru.smirnov.musicplatform.service.abstraction.security.OuterTokenValidatorService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private final TokenGenerator tokenGenerator;
    private final OuterTokenValidatorService outerTokenValidatorService;

    @Autowired
    public AuthenticationController(
            TokenGenerator tokenGenerator,
            OuterTokenValidatorService outerTokenValidatorService
    ) {
        this.tokenGenerator = tokenGenerator;
        this.outerTokenValidatorService = outerTokenValidatorService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> createTokens(@RequestBody @Valid LoginRequest dto) throws Exception {
        return this.tokenGenerator.createTokens(dto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshTokens() {
        return this.tokenGenerator.refreshTokens();
    }

    /*
    // работало
    @PostMapping("/validate")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse validateOuterToken(@RequestBody OuterTokenRequest dto) {
        return this.outerTokenValidatorService.validateOuterToken(dto);
    }
    */

    @PostMapping("/validate")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse validateOuterToken(HttpServletRequest request) {
        String jwtToken = Optional.ofNullable(request.getHeader("Authorization"))
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.substring(7))
                .orElseThrow(() -> new RuntimeException("Authorization header is missing or invalid"));

        return this.outerTokenValidatorService.validateOuterToken(jwtToken);
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
