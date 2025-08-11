package ru.smirnov.musicplatform.entity.auxiliary.embedding;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CommonPersonData {

    @Column(columnDefinition = "TEXT", nullable = false)
    private String lastname;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String firstname;

    @Column(columnDefinition = "CHAR(11)", nullable = false)
    private String phonenumber;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String email;

}
