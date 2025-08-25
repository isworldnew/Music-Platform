package ru.smirnov.musicplatform.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.entity.domain.Track;
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

}
