package ru.smirnov.musicplatform.service.implementation.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.entity.domain.Chart;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.precondition.abstraction.domain.ChartPreconditionService;
import ru.smirnov.musicplatform.repository.relation.SavedChartRepository;
import ru.smirnov.musicplatform.service.abstraction.relation.SavedChartService;

@Service
public class SavedChartServiceImplementation implements SavedChartService {

    private final SavedChartRepository savedChartRepository;
    private final ChartPreconditionService chartPreconditionService;

    @Autowired
    public SavedChartServiceImplementation(
            SavedChartRepository savedChartRepository,
            ChartPreconditionService chartPreconditionService
    ) {
        this.savedChartRepository = savedChartRepository;
        this.chartPreconditionService = chartPreconditionService;
    }

    @Override
    @Transactional
    public Long addChart(Long chartId, DataForToken tokenData) {
        Chart chart = this.chartPreconditionService.getByIdIfExists(chartId);

        if (!chart.getAccessLevel().isAvailable())
            throw new ForbiddenException("Chart (id=" + chartId + ") is not PUBLIC");

        try {
            return this.savedChartRepository.save(tokenData.getEntityId(), chartId);
        }
        catch (DataIntegrityViolationException e) {
            throw new ConflictException("Chart (id=" + chartId + ") already saved by user (id=" + tokenData.getEntityId() + ")");
        }
    }

    @Override
    @Transactional
    public void removeChart(Long chartId, DataForToken tokenData) {
        this.savedChartRepository.delete(tokenData.getEntityId(), chartId);
    }
}
