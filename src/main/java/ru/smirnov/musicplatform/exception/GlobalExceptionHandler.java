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


    @ExceptionHandler(UsernameOccupiedException.class)
    public ResponseEntity<ExceptionDto> handleUsernameOccupiedException(UsernameOccupiedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ExceptionDto(ex.getMessage())
        );
    }

    @ExceptionHandler(PhonenumberOccupiedException.class)
    public ResponseEntity<ExceptionDto> handlePhonenumberOccupiedException(PhonenumberOccupiedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ExceptionDto(ex.getMessage())
        );
    }

    @ExceptionHandler(EmailOccupiedException.class)
    public ResponseEntity<ExceptionDto> handleEmailOccupiedException(EmailOccupiedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ExceptionDto(ex.getMessage())
        );
    }

    @ExceptionHandler(ArtistNameNonUniqueException.class)
    public ResponseEntity<ExceptionDto> handleArtistNameNonUniqueException(ArtistNameNonUniqueException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ExceptionDto(ex.getMessage())
        );
    }


    // ОБЪЕДИНИТЬ ХЕНДЛЕРЫ ПО HTTP-КОДАМ!!! ПРОСТО ОТЛАВЛИВАТЬ ОБОБЩЁННЫЕ ИСКЛЮЧЕНИЯ

}
