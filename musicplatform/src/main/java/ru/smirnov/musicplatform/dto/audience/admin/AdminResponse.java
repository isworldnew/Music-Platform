package ru.smirnov.musicplatform.dto.audience.admin;

import lombok.Data;

@Data
public class AdminResponse {

    private Long id;

    private String username;

    private String lastname;

    private String firstname;

    private String phonenumber;

    private String email;
}
