package ru.smirnov.musicplatform.precondition.implementation.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.entity.domain.Chart;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.exception.NotFoundException;
import ru.smirnov.musicplatform.precondition.abstraction.domain.ChartPreconditionService;
import ru.smirnov.musicplatform.repository.domain.ChartRepository;

@Service
public class ChartPreconditionServiceImplementation implements ChartPreconditionService {

    private final ChartRepository chartRepository;

    @Autowired
    public ChartPreconditionServiceImplementation(ChartRepository chartRepository) {
        this.chartRepository = chartRepository;
    }

    @Override
    public void existsByNameGlobally(String name) {
        Chart chart = this.chartRepository.findByName(name).orElse(null);

        if (chart != null)
            throw new ConflictException("Name '" + name + "' is already in use by chart (id=" + chart.getId() + ")");
    }

    @Override
    public Chart getByIdIfExists(Long chartId) {
        return this.chartRepository.findById(chartId).orElseThrow(
                () -> new NotFoundException("Chart with id=" + chartId + " was not found")
        );
    }

    @Override
    public Chart getByIdIfExistsAndNameIsUniqueGlobally(Long chartId, String name) {
        Chart chartFoundById = this.getByIdIfExists(chartId);

        if (chartFoundById.getName().equals(name))
            return chartFoundById;

        this.existsByNameGlobally(name);

        return chartFoundById;
    }
}
