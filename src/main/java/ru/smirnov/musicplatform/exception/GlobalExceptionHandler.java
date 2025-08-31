package ru.smirnov.musicplatform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.smirnov.musicplatform.dto.exception.ExceptionResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            SecurityContextException.class
    })
    public ResponseEntity<ExceptionResponse> handleUnauthorizedExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ExceptionResponse(ex.getMessage())
        );
    }

    @ExceptionHandler({
            DisabledException.class,
            ForbiddenException.class
    })
    public ResponseEntity<ExceptionResponse> handleForbiddenExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ExceptionResponse(ex.getMessage())
        );
    }

    @ExceptionHandler({
            NonUniqueAccountPerEntity.class,
            UsernameOccupiedException.class,
            PhonenumberOccupiedException.class,
            EmailOccupiedException.class,
            ArtistNameNonUniqueException.class,
            ReferenceConsistencyViolationException.class,
            RelationBetweenArtistAndDistributorException.class,
            ConflictException.class
    })
    public ResponseEntity<ExceptionResponse> handleConflictExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ExceptionResponse(ex.getMessage())
        );
    }

    @ExceptionHandler({
            FileSizeExcessException.class,
            InvalidFileExtensionException.class,
            BadRequestException.class
    })
    public ResponseEntity<ExceptionResponse> handleBadRequestExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
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
            MinioException.class
    })
    public ResponseEntity<ExceptionResponse> handleInternalServerErrorExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ExceptionResponse(ex.getMessage())
        );
    }

}
