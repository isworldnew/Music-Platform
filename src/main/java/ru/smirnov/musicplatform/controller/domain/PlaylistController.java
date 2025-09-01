package ru.smirnov.musicplatform.controller.domain;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/playlists")
@Validated
public class PlaylistController {
}
