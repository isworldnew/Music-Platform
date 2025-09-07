package ru.smirnov.musicplatform.dto.audience.admin;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminRequest {

    @NotBlank
    private String lastname;

    @NotBlank
    private String firstname;

    @NotBlank
    private String phonenumber;

    @NotBlank
    private String email;


    // сделай валидации на номер и почту
}
