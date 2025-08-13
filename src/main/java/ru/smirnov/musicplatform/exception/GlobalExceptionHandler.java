package ru.smirnov.musicplatform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.smirnov.musicplatform.dto.exception.ExceptionDto;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NonUniqueAccountPerEntity.class)
    public ResponseEntity<ExceptionDto> handleNonUniqueAccountPerEntity(NonUniqueAccountPerEntity ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ExceptionDto.builder()
                        .exceptionMessage(ex.getMessage())
                        .build()
        );
    }

}
