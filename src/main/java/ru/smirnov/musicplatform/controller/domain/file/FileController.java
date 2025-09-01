package ru.smirnov.musicplatform.controller.domain.file;

import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.smirnov.musicplatform.service.abstraction.SecurityContextService;
import ru.smirnov.musicplatform.service.abstraction.minio.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/files")
@Validated
public class FileController {

    private final SecurityContextService securityContextService;

    private final ArtistFileManagementService artistFileManagementService;
    private final TrackFileManagementService trackFileManagementService;
    private final AlbumFileManagementService albumFileManagementService;
    private final PlaylistFileManagementService playlistFileManagementService;
    private final ChartFileManagementService chartFileManagementService;

    @Autowired
    public FileController(
            SecurityContextService securityContextService,
            ArtistFileManagementService artistFileManagementService,
            TrackFileManagementService trackFileManagementService,
            AlbumFileManagementService albumFileManagementService,
            PlaylistFileManagementService playlistFileManagementService,
            ChartFileManagementService chartFileManagementService
    ) {
        this.securityContextService = securityContextService;
        this.artistFileManagementService = artistFileManagementService;
        this.trackFileManagementService = trackFileManagementService;
        this.albumFileManagementService = albumFileManagementService;
        this.playlistFileManagementService = playlistFileManagementService;
        this.chartFileManagementService = chartFileManagementService;
    }

    public void


}
