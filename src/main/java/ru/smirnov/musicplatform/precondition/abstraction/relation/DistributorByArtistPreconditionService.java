package ru.smirnov.musicplatform.precondition.abstraction.relation;

public interface DistributorByArtistPreconditionService {

    void checkActiveRelationBetweenDistributorAndArtistExistence(Long distributorId, Long artistId);

}
