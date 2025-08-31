package ru.smirnov.musicplatform.service.implementation.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionAccessLevelRequest;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionRequest;
import ru.smirnov.musicplatform.entity.audience.Admin;
import ru.smirnov.musicplatform.entity.auxiliary.enums.MusicCollectionAccessLevel;
import ru.smirnov.musicplatform.entity.domain.Chart;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.mapper.abstraction.ChartMapper;
import ru.smirnov.musicplatform.precondition.abstraction.domain.ChartPreconditionService;
import ru.smirnov.musicplatform.repository.audience.AdminRepository;
import ru.smirnov.musicplatform.repository.domain.ChartRepository;
import ru.smirnov.musicplatform.service.abstraction.domain.ChartService;

@Service
public class ChartServiceImplementation implements ChartService {

    private final ChartRepository chartRepository;
    private final ChartPreconditionService chartPreconditionService;
    private final ChartMapper chartMapper;

    private final AdminRepository adminRepository;

    @Autowired
    public ChartServiceImplementation(
            ChartRepository chartRepository,
            ChartPreconditionService chartPreconditionService,
            ChartMapper chartMapper,
            AdminRepository adminRepository
    ) {
        this.chartRepository = chartRepository;
        this.chartPreconditionService = chartPreconditionService;
        this.chartMapper = chartMapper;
        this.adminRepository = adminRepository;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Long createChart(MusicCollectionRequest dto, DataForToken tokenData) {
        Admin admin = this.adminRepository.findById(tokenData.getEntityId()).orElseThrow(
                () -> new ForbiddenException("Admin's business-data wasn't found by admin's id in token")
        );
        this.chartPreconditionService.existsByNameGlobally(dto.getName());

        Chart chart = this.chartMapper.musicCollectionRequestToChartEntity(dto, admin);
        this.chartRepository.save(chart);

        return chart.getId();
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void updateChart(Long chartId, MusicCollectionRequest dto, DataForToken tokenData) {
        Chart chart = this.chartPreconditionService.getByIdIfExistsAndNameIsUniqueGlobally(chartId, dto.getName());

        chart.setName(dto.getName());
        chart.setDescription(dto.getDescription());

        this.chartRepository.save(chart);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void updateChartAccessLevel(Long chartId, MusicCollectionAccessLevelRequest dto, DataForToken tokenData) {
        Chart chart = this.chartPreconditionService.getByIdIfExists(chartId);
        chart.setAccessLevel(MusicCollectionAccessLevel.valueOf(dto.getAccessLevel()));
        this.chartRepository.save(chart);

        не нужно ли удалить плейлист из сохранённых, если он был PUBLIC, а стал PRIVATE?
    }

}
