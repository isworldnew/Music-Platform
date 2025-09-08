package ru.smirnov.demandservice.entity.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import ru.smirnov.demandservice.entity.auxiliary.DemandStatus;
import ru.smirnov.demandservice.entity.auxiliary.TTL;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "track_claims")
@Data
public class TrackClaim {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "track_id", nullable = false)
    private Long trackId;

    @Column(name = "admin_id", nullable = false)
    private Long adminId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "VARCHAR(255) DEFAULT 'RECEIVED'", nullable = false)
    private DemandStatus status = DemandStatus.RECEIVED;

    @Column(name = "creation_date", columnDefinition = "TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP", nullable = false)
    @CreationTimestamp
    private OffsetDateTime creationDateTime = OffsetDateTime.now();

    @Column(name = "expiration_date", columnDefinition = "TIMESTAMPTZ", nullable = false)
    private OffsetDateTime expirationDateTime = OffsetDateTime.now().plus(TTL.TRACK_CLAIM.getTimeToLiveInMilliseconds(), ChronoUnit.MILLIS);
}
