package ru.smirnov.musicplatform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.smirnov.musicplatform.dto.exception.ExceptionDto;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NonUniqueAccountPerEntity.class)
    public ResponseEntity<ExceptionDto> handleNonUniqueAccountPerEntity(NonUniqueAccountPerEntity ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            new ExceptionDto(ex.getMessage())
        );
    }

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    public ResponseEntity<ExceptionDto> badCredentialsException(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ExceptionDto(ex.getMessage())
        );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionDto> handleDisabledException(DisabledException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ExceptionDto(ex.getMessage())
        );
    }

}
