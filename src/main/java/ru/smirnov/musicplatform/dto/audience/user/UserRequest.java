package ru.smirnov.musicplatform.dto.audience.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import ru.smirnov.musicplatform.dto.authentication.LoginRequest;

import java.sql.Date;

@Data
public class UserRequest {

    @NotNull @Valid
    private LoginRequest accountData;

    @NotNull @NotBlank @NotEmpty
    private String lastname;

    @NotNull @NotBlank @NotEmpty
    private String firstname;

    @NotNull @JsonFormat(pattern = "dd-MM-yyyy") @Past
    // можно было написать с @AssertTrue или вообще кастомную аннотацию с обработчиком сделать
    // чтобы по какой-нибудь политике из конфигурации проверять возраст
    private Date dateOfBirth;

    @NotNull @NotBlank @NotEmpty
    @Size(min = 11, max = 11, message = "Phone number must be exactly 11 characters")
    private String phonenumber;

    @NotNull @NotBlank @NotEmpty @Email
    private String email;

}
