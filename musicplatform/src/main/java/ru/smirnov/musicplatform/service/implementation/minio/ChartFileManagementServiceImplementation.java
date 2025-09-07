package ru.smirnov.musicplatform.service.implementation.minio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.config.MinioBuckets;
import ru.smirnov.musicplatform.dto.file.ImageFileRequest;
import ru.smirnov.musicplatform.entity.domain.Chart;
import ru.smirnov.musicplatform.exception.MinioException;
import ru.smirnov.musicplatform.precondition.abstraction.domain.ChartPreconditionService;
import ru.smirnov.musicplatform.repository.domain.ChartRepository;
import ru.smirnov.musicplatform.service.abstraction.minio.ChartFileManagementService;
import ru.smirnov.musicplatform.service.abstraction.minio.MinioService;
import ru.smirnov.musicplatform.util.MinioPathUtil;

@Service
public class ChartFileManagementServiceImplementation implements ChartFileManagementService {

    private final MinioService minioService;

    private final ChartPreconditionService chartPreconditionService;
    private final ChartRepository chartRepository;

    @Autowired
    public ChartFileManagementServiceImplementation(
            MinioService minioService,
            ChartPreconditionService chartPreconditionService,
            ChartRepository chartRepository
    ) {
        this.minioService = minioService;
        this.chartPreconditionService = chartPreconditionService;
        this.chartRepository = chartRepository;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void updateChartCover(Long chartId, ImageFileRequest dto, DataForToken tokenData) {
        Chart chart = this.chartPreconditionService.getByIdIfExists(chartId);

        boolean referenceExists = (chart.getImageReference() != null);
        boolean coverIsAttached = (dto.getImageFile() != null && !dto.getImageFile().isEmpty());

        if (!referenceExists && coverIsAttached) {
            String coverReference = MinioPathUtil.generateFormattedReference(MinioBuckets.CHART_COVER.getBucketName(), tokenData.getEntityId(), chartId);
            chart.setImageReference(coverReference);
            try {
                this.minioService.uploadObjectWithMetadata(
                        MinioBuckets.CHART_COVER.getBucketName(),
                        MinioPathUtil.extractObjectName(coverReference),
                        dto.getImageFile(),
                        null
                );
                this.chartRepository.save(chart);
                return;
            }
            catch (Exception e) {
                throw new MinioException("File download to MinIO failed");
            }
        }

        if (referenceExists && !coverIsAttached) {
            String coverReference = chart.getImageReference();
            chart.setImageReference(null);
            try {
                this.minioService.removeObject(
                        MinioBuckets.CHART_COVER.getBucketName(),
                        MinioPathUtil.extractObjectName(coverReference)
                );
                this.chartRepository.save(chart);
                return;
            }
            catch (Exception e) {
                throw new MinioException("File download to MinIO failed");
            }
        }

        if (referenceExists && coverIsAttached) {
            String coverReference = chart.getImageReference();
            try {
                this.minioService.replaceObjectInBucket(
                        MinioBuckets.CHART_COVER.getBucketName(),
                        MinioPathUtil.extractObjectName(coverReference),
                        dto.getImageFile()
                );
            }
            catch (Exception e) {
                throw new MinioException("File download to MinIO failed");
            }
        }

    }
}
