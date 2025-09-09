package ru.smirnov.dtoregistry.message;

import lombok.Data;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.dtoregistry.entity.auxiliary.TrackStatus;

@Data
public class TrackStatusMessage {

    private Long trackId;

    private TrackStatus status;

    private DataForToken tokenData;
}
