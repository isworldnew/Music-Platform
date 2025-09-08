package ru.smirnov.demandservice.client.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.smirnov.demandservice.client.abstraction.TrackClient;
import ru.smirnov.demandservice.config.MusicPlatformServiceConfig;
import ru.smirnov.dtoregistry.exception.NotFoundException;

@Service
public class TrackClientImplementation implements TrackClient {

    private final RestTemplate restTemplate;
    private final String musicPlatformServiceUrl;

    @Autowired
    public TrackClientImplementation(RestTemplate restTemplate, MusicPlatformServiceConfig musicPlatformServiceConfig) {
        this.restTemplate = restTemplate;
        this.musicPlatformServiceUrl = musicPlatformServiceConfig.getMusicPlatformServiceUrl();
    }

    @Override
    public void trackExistsById(Long trackId) {
        String url = this.musicPlatformServiceUrl + "/tracks/" + trackId + "/existence";

        try {
            ResponseEntity<Void> response = this.restTemplate.getForEntity(url, Void.class);
        }
        catch (HttpClientErrorException.NotFound ex) {
            throw new NotFoundException("Track with id=" + trackId + " was not found");
        }
    }

}
