package ru.smirnov.musicplatform.precondition.implementation.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.entity.domain.Track;
import ru.smirnov.musicplatform.exception.ConflictException;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.exception.NotFoundException;
import ru.smirnov.musicplatform.precondition.abstraction.domain.TrackPreconditionService;
import ru.smirnov.musicplatform.repository.domain.TrackRepository;

import java.util.List;

@Service
public class TrackPreconditionServiceImplementation implements TrackPreconditionService {

    private final TrackRepository trackRepository;

    @Autowired
    public TrackPreconditionServiceImplementation(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @Override
    public void existsByName(Long artistId, String name) {
        Track track = this.trackRepository.findByNameAndArtistId(name, artistId).orElse(null);

        if (track != null)
            throw new ConflictException("Artist (id=" + artistId + ") already has track (id=" + track.getId() + ") with name='" + name + "'");
    }

    @Override
    public Track getByIdIfExists(Long trackId) {
        return this.trackRepository.findById(trackId).orElseThrow(
                () -> new NotFoundException("Track with id=" + trackId + " was not found")
        );
    }

    @Override
    public Track getByIdIfExistsAndNameIsUniquePerArtist(Long trackId, String name) {

        /*
        Комплексная проверка:
        [v] Трек с данным id существует
        [v] Имя, пришедшее в запросе на обновление, либо уже приналежит ему (то есть оно не обновилось)
        [v] Либо оно уникально для исполнителя данного трека
        */

        Track trackFoundById = this.getByIdIfExists(trackId);

        if (trackFoundById.getName().equals(name)) return trackFoundById;

        // второй запрос и не понадобится, если имена совпали
        // так... тут как будто изляшняя проверка, ведь, если имена не совпали, то имя уже автоматически новое
        // и нужно просто убедиться, что у исполнителя таких имён нет (без сверки id)
        Track trackFoundByName = this.trackRepository.findByNameAndArtistId(name, trackFoundById.getArtist().getId()).orElse(null);

        if (trackFoundByName != null)
            throw new ConflictException(
                    "Artist (id=" + trackFoundById.getArtist().getId() + ") already has track (id=" + trackFoundByName.getId() + ") with name='" + name + "'"
            );

        return trackFoundById;
    }

    @Override
    public Track getIfOwnedOrCollaboratedByArtist(Long trackId, Long artistId) {
        /*
        метод, который комплексно проверяет существование трека и его принадлежность к артисту (он должен быть автором или соавтором)
        */
        Track track = this.getByIdIfExists(trackId);

        List<Long> coArtists = track.getCoArtists().stream().map(coArtist -> coArtist.getArtist().getId()).toList();

        if (!track.getArtist().getId().equals(artistId) && !coArtists.contains(artistId))
            throw new ForbiddenException("Track (id=" + track.getId() + ") doesn't owned or collaborated by artist (id=" + artistId + ")");

        return track;
    }
}
