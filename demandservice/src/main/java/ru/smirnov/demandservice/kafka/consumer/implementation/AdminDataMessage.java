package ru.smirnov.demandservice.kafka.consumer.implementation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.smirnov.dtoregistry.entity.auxiliary.AccountStatus;

@Data @AllArgsConstructor @NoArgsConstructor
public class AdminDataMessage {

    private Long id;

    private AccountStatus accountStatus;

}
