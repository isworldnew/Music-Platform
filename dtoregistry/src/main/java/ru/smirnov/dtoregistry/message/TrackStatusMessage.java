package ru.smirnov.dtoregistry.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.smirnov.dtoregistry.entity.auxiliary.TrackStatus;

@Data @AllArgsConstructor @NoArgsConstructor
public class TrackStatusMessage {

    private Long trackId;

    private TrackStatus status;

}
