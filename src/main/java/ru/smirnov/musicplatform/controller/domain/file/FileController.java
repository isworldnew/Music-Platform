package ru.smirnov.musicplatform.controller.domain.file;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.file.AudioFileRequest;
import ru.smirnov.musicplatform.dto.file.ImageFileRequest;
import ru.smirnov.musicplatform.service.abstraction.SecurityContextService;
import ru.smirnov.musicplatform.service.abstraction.minio.*;


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

    @PostMapping("/artists/{id}/cover")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public void updateArtistCover(@NotNull @Positive @PathVariable("id") Long artistId, @ModelAttribute @Valid ImageFileRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.artistFileManagementService.updateArtistCover(artistId, dto, tokenData);
    }

    @PostMapping("/tracks/{id}/cover")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public void updateTrackCover(@NotNull @Positive @PathVariable("id") Long trackId, @ModelAttribute @Valid ImageFileRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.trackFileManagementService.updateTrackCover(trackId, dto, tokenData);
    }

    @PostMapping("/tracks/{id}/audio")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public void updateTrackAudio(@NotNull @Positive @PathVariable("id") Long trackId, @ModelAttribute @Valid AudioFileRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.trackFileManagementService.updateTrackAudio(trackId, dto, tokenData);
    }

    @PostMapping("/albums/{id}/cover")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public void updateAlbumCover(@NotNull @Positive @PathVariable("id") Long albumId, @ModelAttribute @Valid ImageFileRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.albumFileManagementService.updateAlbumCover(albumId, dto, tokenData);
    }

    @PostMapping("/playlists/{id}/cover")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER')")
    public void updatePlaylistCover(@NotNull @Positive @PathVariable("id") Long playlistId, @ModelAttribute @Valid ImageFileRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.playlistFileManagementService.updatePlaylistCover(playlistId, dto, tokenData);
    }

    @PostMapping("/charts/{id}/cover")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public void updateChartCover(@NotNull @Positive @PathVariable("id") Long chartId, @ModelAttribute @Valid ImageFileRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.chartFileManagementService.updateChartCover(chartId, dto, tokenData);
    }

}
