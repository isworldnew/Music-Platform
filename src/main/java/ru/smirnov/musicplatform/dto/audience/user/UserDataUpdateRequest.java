package ru.smirnov.musicplatform.dto.audience.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.sql.Date;

public class UserDataUpdateRequest {

    @NotBlank
    private String lastname;

    @NotBlank
    private String firstname;

    @NotNull @JsonFormat(pattern = "dd-MM-yyyy") @Past
    // можно было написать с @AssertTrue или вообще кастомную аннотацию с обработчиком сделать
    // чтобы по какой-нибудь политике из конфигурации проверять возраст
    private Date dateOfBirth;

    @NotBlank
    @Size(min = 11, max = 11, message = "Phone number must be exactly 11 characters")
    private String phonenumber;

    @NotBlank @Email
    private String email;
}
