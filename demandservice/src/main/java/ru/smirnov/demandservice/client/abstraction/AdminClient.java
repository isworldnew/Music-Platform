package ru.smirnov.demandservice.client.abstraction;

import java.util.List;

public interface AdminClient {
    List<Long> getAllEnabledAdmins();
}
