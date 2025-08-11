package ru.smirnov.musicplatform.entity.audience;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.smirnov.musicplatform.entity.auxiliary.enums.AccountStatus;
import ru.smirnov.musicplatform.entity.auxiliary.enums.Role;

@Entity
@Table(name = "accounts")
@Data @NoArgsConstructor
public class Account {

    // тут ещё должна быть зависимость, нужная для сущности, но ненужная для БД
    // зависимость от менеджера паролей

    @Id @GeneratedValue
    private Long id;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String username;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    @OneToOne(mappedBy = "account")
    @JsonManagedReference
    private User user;

    @OneToOne(mappedBy = "account")
    @JsonManagedReference
    private Admin admin;

    @OneToOne(mappedBy = "account")
    @JsonManagedReference
    private Distributor distributor;

}
