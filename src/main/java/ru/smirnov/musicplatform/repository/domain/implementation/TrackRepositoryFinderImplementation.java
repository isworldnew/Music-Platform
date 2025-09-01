//package ru.smirnov.musicplatform.repository.domain.implementation;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.criteria.*;
//import org.springframework.stereotype.Repository;
//import ru.smirnov.musicplatform.entity.domain.Artist;
//import ru.smirnov.musicplatform.entity.domain.Track;
//import ru.smirnov.musicplatform.entity.relation.CoArtists;
//import ru.smirnov.musicplatform.repository.domain.finder.TrackRepositoryFinder;
//
//import java.util.List;
//
//@Repository
//public class TrackRepositoryFinderImplementation implements TrackRepositoryFinder {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Override
//    public List<Track> searchTracks(String searchRequest) {
//
//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//
//        CriteriaQuery<Track> criteriaQuery = criteriaBuilder.createQuery(Track.class);
//
//        Root<Track> trackRoot = criteriaQuery.from(Track.class);
//
//        Join<Track, Artist> trackJoinArtist = trackRoot.join("artist", JoinType.);
//        Join<Track, CoArtists> trackJoinCoArtist = trackRoot.join("coArtists", JoinType.);
//        Join<CoArtists, Artist> coArtistJoinArtist = trackJoinArtist.join("artist", JoinType.);
//
//    }
//}
