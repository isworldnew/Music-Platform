package ru.smirnov.musicplatform.controller.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.musicplatform.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistRequest;
import ru.smirnov.musicplatform.dto.domain.artist.ArtistResponse;
import ru.smirnov.musicplatform.dto.domain.artist.ExtendedArtistResponse;
import ru.smirnov.musicplatform.dto.domain.musiccollection.MusicCollectionRequest;
import ru.smirnov.musicplatform.dto.domain.track.TrackRequest;
import ru.smirnov.musicplatform.dto.relation.ArtistSocialNetworkRequest;
import ru.smirnov.musicplatform.service.abstraction.relation.ArtistSocialNetworkService;
import ru.smirnov.musicplatform.service.abstraction.security.SecurityContextService;
import ru.smirnov.musicplatform.service.abstraction.domain.AlbumService;
import ru.smirnov.musicplatform.service.abstraction.domain.ArtistService;
import ru.smirnov.musicplatform.service.abstraction.domain.TrackService;

@RestController
@RequestMapping("/artists")
@Validated
public class ArtistController {

    private final SecurityContextService securityContextService;

    private final ArtistService artistService;
    private final TrackService trackService;
    private final AlbumService albumService;
    private final ArtistSocialNetworkService artistSocialNetworkService;

    @Autowired
    public ArtistController(
            SecurityContextService securityContextService,
            ArtistService artistService,
            TrackService trackService,
            AlbumService albumService,
            ArtistSocialNetworkService artistSocialNetworkService
    ) {
        this.securityContextService = securityContextService;
        this.artistService = artistService;
        this.trackService = trackService;
        this.albumService = albumService;
        this.artistSocialNetworkService = artistSocialNetworkService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public Long createArtist(@RequestBody @Valid ArtistRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.artistService.createArtist(dto, tokenData);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public void updateArtist(@NotNull @Positive @PathVariable("id") Long artistId, @RequestBody @Valid ArtistRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        this.artistService.updateArtist(artistId, dto, tokenData);
    }

    @GetMapping("/{id}/extended")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('DISTRIBUTOR', 'ADMIN')")
    public ExtendedArtistResponse getArtistWithDetails(@NotNull @Positive @PathVariable("id") Long artistId) {
        return this.artistService.getExtendedArtistDataById(artistId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER', 'ANONYMOUS')")
    public ArtistResponse getArtist(@NotNull @Positive @PathVariable("id") Long artistId) {
        return this.artistService.getArtistDataById(artistId);
    }

    @PostMapping("/{id}/tracks")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public Long uploadTrack(@NotNull @Positive @PathVariable("id") Long artistId, @RequestBody @Valid TrackRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.trackService.uploadTrack(artistId, dto, tokenData);
    }

    @PostMapping("/{id}/albums")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public Long createAlbum(@NotNull @Positive @PathVariable("id") Long artistId, @RequestBody @Valid MusicCollectionRequest dto) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.albumService.createAlbum(artistId, dto, tokenData);
    }

    @PostMapping("/{id}/social-networks")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public Long addSocialNetwork(
            @NotNull @Positive @PathVariable("id") Long artistId,
            @RequestBody @Valid ArtistSocialNetworkRequest dto
    ) {
        DataForToken tokenData = this.securityContextService.safelyExtractTokenDataFromSecurityContext();
        return this.artistSocialNetworkService.addSocialNetwork(artistId, dto, tokenData);
    }
}
