package ru.smirnov.demandservice.repository.finder.abstraction;

import ru.smirnov.demandservice.projection.abstraction.AdminClaimProjection;

import java.util.List;

public interface ClaimByAdminFinderRepository {
    List<AdminClaimProjection> getClaimPerAdminStats();
}
