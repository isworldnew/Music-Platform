package ru.smirnov.musicplatform.dto.audience.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.smirnov.dtoregistry.dto.authentication.LoginRequest;

@Data
public class AdminRegistrationRequest {

    @NotNull @Valid
    private LoginRequest accountData;

    @NotBlank
    private String lastname;

    @NotBlank
    private String firstname;

    @NotBlank
    @Size(min = 11, max = 11, message = "Phone number must be exactly 11 characters")
    private String phonenumber;

    @NotBlank @Email
    private String email;
}
