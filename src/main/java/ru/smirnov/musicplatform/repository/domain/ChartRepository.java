package ru.smirnov.musicplatform.repository.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.domain.Chart;

import java.util.Optional;

@Repository
public interface ChartRepository extends JpaRepository<Chart, Long> {

    Optional<Chart> findByName(String name);

}
