package ru.smirnov.musicplatform.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.musicplatform.authentication.TokenGenerator;
import ru.smirnov.musicplatform.dto.audience.user.UserPersonalDataRegistrationDto;
import ru.smirnov.musicplatform.dto.authentication.JwtResponseDto;
import ru.smirnov.musicplatform.dto.authentication.LoginRequestDto;
import ru.smirnov.musicplatform.entity.audience.User;
import ru.smirnov.musicplatform.service.sql.audience.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final TokenGenerator tokenGenerator;

    @Autowired
    public UserController(UserService userService, TokenGenerator tokenGenerator) {
        this.userService = userService;
        this.tokenGenerator = tokenGenerator;
    }

    @PostMapping("/registration")
    public ResponseEntity<JwtResponseDto> userRegistration(@RequestBody @Valid UserPersonalDataRegistrationDto dto) {
        User user = this.userService.userRegistration(dto);
        return this.tokenGenerator.createTokens(
                new LoginRequestDto(dto.getAccountData().getUsername(), dto.getAccountData().getPassword())
        );
    }


    // наверное нужно раздельное обновление бизнес-данных об аккаунте и аккаунта (юзернейм, пароль)


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
