package ru.smirnov.musicplatform.service.implementation.minio;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.tmp.ImageFileRequest;
import ru.smirnov.musicplatform.precondition.abstraction.domain.ChartPreconditionService;
import ru.smirnov.musicplatform.repository.domain.ChartRepository;
import ru.smirnov.musicplatform.service.abstraction.minio.ChartFileManagementService;
import ru.smirnov.musicplatform.service.abstraction.minio.MinioService;

@Service
public class ChartFileManagementServiceImplementation implements ChartFileManagementService {

    private final MinioService minioService;

    private final ChartPreconditionService chartPreconditionService;
    private final ChartRepository chartRepository;

    @Override
    @Transactional
    public void updateChartCover(Long chartId, ImageFileRequest dto, DataForToken tokenData) {

    }
}
