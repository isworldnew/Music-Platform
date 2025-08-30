package ru.smirnov.musicplatform.validators.old;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.entity.domain.Track;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.exception.NotFoundException;
import ru.smirnov.musicplatform.repository.domain.TrackRepository;

@Service
public class TrackValidator {

    private final TrackRepository trackRepository;

    @Autowired
    public TrackValidator(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    public Track safelyGetById(Long trackId) {
        Track track = this.trackRepository.findById(trackId).orElse(null);

        if (track == null)
            throw new NotFoundException("No track found with id=" + trackId);

        return track;
    }

    public Track safelyGetByIdWithActiveStatus(Long trackId) {
        Track track = this.safelyGetById(trackId);

        if (!track.getStatus().isAvailable())
            throw new ForbiddenException("Unable to refer the track with id=" + trackId + " because of it's status: " + track.getStatus().name());

        return track;
    }
}
