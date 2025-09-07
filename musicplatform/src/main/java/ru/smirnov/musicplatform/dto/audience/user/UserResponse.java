package ru.smirnov.musicplatform.dto.audience.user;

import lombok.Data;

import java.sql.Date;
import java.time.OffsetDateTime;

@Data
public class UserResponse {

    private Long userId;

    private Long accountId;

    private String username;

    private String lastname;

    private String firstname;

    private Date dateOfBirth;

    private String phonenumber;

    private String email;

    private OffsetDateTime registrationDate;
}
