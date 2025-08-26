package ru.smirnov.musicplatform.repository.domain;

import org.springframework.stereotype.Repository;
import ru.smirnov.musicplatform.entity.domain.Chart;
import ru.smirnov.musicplatform.repository.auxiliary.MusicCollectionRepository;

@Repository
public interface ChartRepository extends MusicCollectionRepository<Chart, Long> {
}
