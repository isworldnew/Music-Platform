package ru.smirnov.musicplatform.dto.domain.artist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class ArtistShortcutResponse {

    private Long id;

    private String name;

}
