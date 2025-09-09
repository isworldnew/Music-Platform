package ru.smirnov.demandservice.entity.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "admins_data")
@Data
public class AdminData {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "admin_id", nullable = false)
    private Long adminId;

    // нужен его статус? если да, то boolean или enum?
}
