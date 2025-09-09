package ru.smirnov.dtoregistry.message;

import lombok.Data;
import ru.smirnov.dtoregistry.entity.auxiliary.AccountStatus;

@Data
public class AdminDataMessage {

    private Long id;

    private AccountStatus accountStatus;

}
