package ru.smirnov.dtoregistry.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// из:
// package ru.smirnov.musicplatform.dto.authentication;

@Data @AllArgsConstructor @NoArgsConstructor
public class LoginRequest {

    @NotBlank
    @Size(min = 4, message = "Username's size should be >= 4")
    private String username;

    @NotBlank
    @Size(min = 10, message = "Password's size should be >= 10")
    private String password;

}
