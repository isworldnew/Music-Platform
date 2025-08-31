package ru.smirnov.musicplatform.precondition.abstraction.domain;

import ru.smirnov.musicplatform.entity.domain.Chart;

public interface ChartPreconditionService {

    void existsByNameGlobally(String name);

    Chart getByIdIfExists(Long chartId);

    Chart getByIdIfExistsAndNameIsUniqueGlobally(Long chartId, String name);
}
