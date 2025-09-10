package ru.smirnov.demandservice.controller.exception;

import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.smirnov.dtoregistry.dto.authentication.ExceptionResponse;
import ru.smirnov.dtoregistry.exception.*;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler({
            JwtException.class,
            SecurityContextException.class
    })
    public ResponseEntity<ExceptionResponse> handleInternalServerErrorExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ExceptionResponse(ex.getMessage())
        );
    }

    @ExceptionHandler({
            ForbiddenException.class
    })
    public ResponseEntity<ExceptionResponse> handleForbiddenExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ExceptionResponse(ex.getMessage())
        );
    }

    @ExceptionHandler({
            ExternalServiceException.class
    })
    public ResponseEntity<ExceptionResponse> handleServerUnavailableExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                new ExceptionResponse(ex.getMessage())
        );
    }

    @ExceptionHandler({
            NotFoundException.class
    })
    public ResponseEntity<ExceptionResponse> handleNotFoundExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionResponse(ex.getMessage())
        );
    }

    @ExceptionHandler({
            BadRequestException.class
    })
    public ResponseEntity<ExceptionResponse> handleBadRequestExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionResponse(ex.getMessage())
        );
    }

    @ExceptionHandler({
            ConflictException.class
    })
    public ResponseEntity<ExceptionResponse> handleConflictExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ExceptionResponse(ex.getMessage())
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
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity.badRequest().body("Validation failed: " + ex.getMessage());
    }

}
