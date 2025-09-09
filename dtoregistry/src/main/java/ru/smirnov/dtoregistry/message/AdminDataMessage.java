package ru.smirnov.dtoregistry.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.smirnov.dtoregistry.entity.auxiliary.AccountStatus;

@Data @AllArgsConstructor
public class AdminDataMessage {

    private Long id;

    private AccountStatus accountStatus;

}
