package ru.smirnov.demandservice.service.implementation.auxiliary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.smirnov.demandservice.projection.abstraction.AdminClaimProjection;
import ru.smirnov.demandservice.projection.implementation.AdminClaimProjectionImplementation;
import ru.smirnov.demandservice.repository.AdminDataRepository;
import ru.smirnov.demandservice.repository.DistributorRegistrationClaimRepository;
import ru.smirnov.demandservice.repository.TrackClaimRepository;
import ru.smirnov.demandservice.service.abstraction.auxiliary.ClaimAssignService;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service @Primary
public class StrategyClaimAssignService implements ClaimAssignService {

    private final TrackClaimRepository trackClaimRepository;
    private final DistributorRegistrationClaimRepository distributorRegistrationClaimRepository;
    private final AdminDataRepository adminDataRepository;

    @Autowired
    public StrategyClaimAssignService(
            TrackClaimRepository trackClaimRepository,
            DistributorRegistrationClaimRepository distributorRegistrationClaimRepository,
            AdminDataRepository adminDataRepository
    ) {
        this.trackClaimRepository = trackClaimRepository;
        this.distributorRegistrationClaimRepository = distributorRegistrationClaimRepository;
        this.adminDataRepository = adminDataRepository;
    }

    @Override
    public Long assignTo() {
        // вообще все администраторы: часть из них может быть без заявок
        List<Long> allAdmins = this.adminDataRepository.findAll()
                .stream()
                .map(admin -> admin.getAdminId())
                .toList();

        // информация об админах с актуальными заявками
        List<AdminClaimProjection> claimPerAdminRawStats = this.adminDataRepository.getClaimPerAdminStats();

        // полная информация о том, сколько на кого приходится заявок
        Map<Long, Long> claimPerAdminStats = new HashMap<>();

        claimPerAdminRawStats.forEach(rawStats -> claimPerAdminStats.put(rawStats.getId(), rawStats.getAmountOfClaims()));

        allAdmins.forEach(admin -> claimPerAdminStats.computeIfAbsent(admin, value -> 0L));

        // удобоваримый вид для сортировки
        List<AdminClaimProjectionImplementation> sortedStats = claimPerAdminStats.entrySet()
                .stream()
                .map(stat -> new AdminClaimProjectionImplementation(stat.getKey(), stat.getValue()))
                .sorted(Comparator.comparingLong(stat -> stat.getAmountOfClaims()))
                .toList();

        return sortedStats.get(0).getId();
    }

}
