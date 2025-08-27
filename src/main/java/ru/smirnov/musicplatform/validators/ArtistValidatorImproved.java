package ru.smirnov.musicplatform.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.musicplatform.entity.domain.Artist;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.exception.NotFoundException;
import ru.smirnov.musicplatform.exception.RelationBetweenArtistAndDistributorException;
import ru.smirnov.musicplatform.repository.domain.ArtistRepository;
import ru.smirnov.musicplatform.repository.relation.DistributorByArtistRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ArtistValidatorImproved {

    // не понравилось то, как реализовал старый валидатор (ArtistValiadator)
    // пока просто пишу... потом буду рефакторить

    // дабы не ломать уже написанное, создам пока новый, потом исправлю

    private final ArtistRepository artistRepository;
    private final DistributorByArtistRepository distributorByArtistRepository;

    @Autowired
    public ArtistValidatorImproved(
            ArtistRepository artistRepository,
            DistributorByArtistRepository distributorByArtistRepository
    ) {
        this.artistRepository = artistRepository;
        this.distributorByArtistRepository = distributorByArtistRepository;
    }


    public Artist safelyGetById(Long artistId) {
        Artist artist = this.artistRepository.findById(artistId).orElse(null);

        if (artist == null)
            throw new NotFoundException("Not found artist with id=" + artistId);

        return artist;
    }

    public Artist distributorIsAbleToInteractWithThisArtist(Long targetDistributorId, Long artistId) {

        // после этой проверки точно должны быть уверены, что Artist с таким id существует
        Artist artist = this.safelyGetById(artistId);

        Integer amountOfActiveRelations = this.distributorByArtistRepository.countAmountOfActiveRelations(artistId).orElse(null);

        if (amountOfActiveRelations == null || amountOfActiveRelations.equals(0))
            throw new RelationBetweenArtistAndDistributorException("Artist with id=" + artistId + " has [0] 'ACTIVE' relations");
        
        if (amountOfActiveRelations > 1)
            throw new RelationBetweenArtistAndDistributorException(
                    "CRITICAL DATA COLLISION: Artist with id=" + artistId + " has [" + amountOfActiveRelations + "] 'ACTIVE' relations"
            );
        
        Long foundDistributorId = this.distributorByArtistRepository.activeDistributionStatusWithArtist(artistId).orElse(null);

        // System.out.println("TARGET DISTRIBUTOR ID = " + targetDistributorId + "; FOUND DISTRIBUTOR ID = " + foundDistributorId);

        if (!targetDistributorId.equals(foundDistributorId))
            throw new ForbiddenException("Distributor with id=" + targetDistributorId + " has no rights to manage artist with id=" + artistId);

        return artist;
    }

    public Artist distributorIsAbleToInteractWithThisArtist(Long targetDistributorId, Long artistId, Set<Long> tracks) {

        Artist artist = this.distributorIsAbleToInteractWithThisArtist(targetDistributorId, artistId);

        List<Long> ownTracksOfArtist = artist.getTracks().stream().map(track -> track.getId()).toList();
        List<Long> partnershipTracksOfArtist = artist.getCoAuthorshipTracks().stream().map(coArtist -> coArtist.getTrack().getId()).toList();

        List<Long> tracksOfArtist = Stream.concat(ownTracksOfArtist.stream(), partnershipTracksOfArtist.stream()).toList();

        boolean allTracksBelongToManagedArtist = tracks.stream().allMatch(track -> tracksOfArtist.contains(track));

        if (!allTracksBelongToManagedArtist && !tracksOfArtist.isEmpty())
//            throw new ForbiddenException("There are tracks (" + tracks + ") which don't belong to the managed artist and he is not co-author of them (" + tracksOfArtist + ")");
            throw new ForbiddenException("There are tracks which don't belong to the managed artist and he is not co-author of them");

        return artist;

    }

}
