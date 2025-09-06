package ru.smirnov.musicplatform.finder.abstraction;

import ru.smirnov.musicplatform.dto.audience.distributor.DistributorShortcutResponse;

import java.util.List;

public interface DistributorFinderService {

    List<DistributorShortcutResponse> searchDistributors(String searchRequest);

}
