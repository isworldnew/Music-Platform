package ru.smirnov.dtoregistry.dto.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.smirnov.dtoregistry.validation.annotation.DemandStatus;

@Data @AllArgsConstructor @NoArgsConstructor
public class DemandStatusRequest {

    @NotBlank @DemandStatus
    private String demandStatus;
}
