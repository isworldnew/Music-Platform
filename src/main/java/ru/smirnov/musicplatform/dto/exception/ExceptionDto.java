package ru.smirnov.musicplatform.dto.exception;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class ExceptionDto {

    private String exceptionMessage;

}
