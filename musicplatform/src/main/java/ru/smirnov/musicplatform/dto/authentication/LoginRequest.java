package ru.smirnov.musicplatform.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class LoginRequest {

    @NotNull @NotBlank @NotEmpty
    private String username;

    @NotNull @NotBlank @NotEmpty
    @Size(min = 10, message = "Password's size should be >= 10")
    private String password;

}
