package ru.smirnov.musicplatform.dto.audience.admin;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class AdminRequest {

    private String lastname;

    private String firstname;

    private String phonenumber;
    сделай валидации на номер и почту
    private String email;

}
