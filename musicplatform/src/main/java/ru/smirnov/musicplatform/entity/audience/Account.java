package ru.smirnov.musicplatform.entity.audience;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.smirnov.dtoregistry.entity.auxiliary.AccountStatus;import ru.smirnov.musicplatform.entity.auxiliary.enums.Role;

@Entity
@Table(name = "accounts")
@Data @NoArgsConstructor
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false, unique = true)
    @Size(min = 3, max = 255, message = "Username length must be in range [3, 255] characters")
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
