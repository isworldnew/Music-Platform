package ru.smirnov.musicplatform.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JwtRequestDto {

    @NotNull @NotBlank @NotEmpty
    private String username;

    @NotNull @NotBlank @NotEmpty
    private String password;

}
