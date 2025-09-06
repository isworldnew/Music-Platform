package ru.smirnov.musicplatform.dto.audience.distributor;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class DistributedArtistShortcutResponse {

    private Long id;

    private Long artistId;

    private String name;

    private String distributionStatus;
}
