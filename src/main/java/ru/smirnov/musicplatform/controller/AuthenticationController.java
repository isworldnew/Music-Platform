package ru.smirnov.musicplatform.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.musicplatform.authentication.TokenManager;
import ru.smirnov.musicplatform.dto.authentication.JwtRequestDto;
import ru.smirnov.musicplatform.dto.authentication.JwtResponseDto;
import ru.smirnov.musicplatform.service.AccountService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final TokenManager tokenManager;

    @Autowired
    public AuthenticationController(
            AccountService accountService,
            AuthenticationManager authenticationManager,
            TokenManager tokenManager
    ) {
        this.accountService = accountService;
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> createToken(@RequestBody @Valid JwtRequestDto dto) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );
        }
        catch (DisabledException dee) {
            throw new Exception("User is disabled", dee);
        }
        catch (BadCredentialsException bce) {
            throw new Exception("Invalid credentials (username or password)", bce);
        }

        UserDetails dataForToken = this.accountService.loadUserByUsername(dto.getUsername());

        String token = this.tokenManager.generateJwtToken(dataForToken);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new JwtResponseDto(token)
        );

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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity.badRequest().body("Validation failed: " + ex.getMessage());
    }

}
