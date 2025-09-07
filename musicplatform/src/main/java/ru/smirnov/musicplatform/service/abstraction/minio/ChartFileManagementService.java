package ru.smirnov.musicplatform.service.abstraction.minio;

import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.file.ImageFileRequest;

public interface ChartFileManagementService {

    void updateChartCover(Long chartId, ImageFileRequest dto, DataForToken tokenData);

}
