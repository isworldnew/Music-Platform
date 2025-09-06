package ru.smirnov.musicplatform.repository.audience.finder.abstraction;

import ru.smirnov.musicplatform.entity.audience.Distributor;

import java.util.List;

public interface DistributorFinderRepository {

    List<Distributor> searchDistributors(String searchRequest);

}
