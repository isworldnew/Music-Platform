package ru.smirnov.demandservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smirnov.demandservice.entity.domain.AdminData;

@Repository
public interface AdminDataRepository extends JpaRepository<AdminData, Long> {
}
