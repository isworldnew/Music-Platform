package ru.smirnov.musicplatform.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.entity.domain.Chart;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.exception.NotFoundException;
import ru.smirnov.musicplatform.repository.domain.ChartRepository;

@Component
public class ChartValidator {

    private final ChartRepository chartRepository;

    @Autowired
    public ChartValidator(ChartRepository chartRepository) {
        this.chartRepository = chartRepository;
    }

    public Chart getChartByIdSafely(Long chartId) {
        return this.chartRepository.findById(chartId).orElseThrow(() -> new NotFoundException("No chart found with id=" + chartId));
    }

    public void checkChartExistenceByName(String name) {
        Chart chart = this.chartRepository.findByName(name).orElse(null);

        if (chart != null)
            throw new ConflictException("Chart with name='" + name + "' already exists");
    }

    public Chart checkChartExistenceByNameForUpdate(String name, Long chartId) {
        Chart chart = this.chartRepository.findByName(name).orElse(null);

        if (chart != null && !chart.getId().equals(chartId))
            throw new ConflictException("Chart with name='" + name + "' already exists");

        return chart;
    }
}
