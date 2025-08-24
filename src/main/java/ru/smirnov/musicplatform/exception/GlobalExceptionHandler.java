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

    @ExceptionHandler({
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            SecurityContextException.class
    })
    public ResponseEntity<ExceptionDto> handleUnauthorizedExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ExceptionDto(ex.getMessage())
        );
    }

    @ExceptionHandler({
            DisabledException.class,
            ForbiddenException.class
    })
    public ResponseEntity<ExceptionDto> handleForbiddenExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ExceptionDto(ex.getMessage())
        );
    }

    @ExceptionHandler({
            NonUniqueAccountPerEntity.class,
            UsernameOccupiedException.class,
            PhonenumberOccupiedException.class,
            EmailOccupiedException.class,
            ArtistNameNonUniqueException.class,
            ReferenceConsistencyViolationException.class,
            RelationBetweenArtistAndDistributorException.class
    })
    public ResponseEntity<ExceptionDto> handleConflictExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ExceptionDto(ex.getMessage())
        );
    }

    @ExceptionHandler({
            FileSizeExcessException.class,
            InvalidFileExtensionException.class,
            BadRequestException.class
    })
    public ResponseEntity<ExceptionDto> handleBadRequestExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionDto(ex.getMessage())
        );
    }

    @ExceptionHandler({
            NotFoundException.class
    })
    public ResponseEntity<ExceptionDto> handleNotFoundExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionDto(ex.getMessage())
        );
    }

}
