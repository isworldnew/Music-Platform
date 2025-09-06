package ru.smirnov.musicplatform.dto.audience.distributor;

import lombok.Data;
import ru.smirnov.musicplatform.entity.auxiliary.enums.DistributorType;

@Data
public class DistributorShortcutResponse {

    private Long id;

    private String name;

    private String distributorType;
}
