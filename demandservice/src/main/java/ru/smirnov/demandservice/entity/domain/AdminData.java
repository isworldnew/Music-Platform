package ru.smirnov.demandservice.entity.domain;

import jakarta.persistence.*;
import lombok.Data;
import ru.smirnov.dtoregistry.entity.auxiliary.AccountStatus;

@Entity
@Table(name = "admins_data")
@Data
public class AdminData {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "admin_id", nullable = false)
    private Long adminId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;
}
