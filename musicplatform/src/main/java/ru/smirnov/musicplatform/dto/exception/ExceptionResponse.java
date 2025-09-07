package ru.smirnov.musicplatform.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ExceptionResponse {

    private String exceptionMessage;

}
