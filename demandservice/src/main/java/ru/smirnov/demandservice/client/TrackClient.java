package ru.smirnov.demandservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.smirnov.demandservice.config.MusicPlatformServiceConfig;
import ru.smirnov.dtoregistry.exception.NotFoundException;

@Service
public class TrackClient {

    private final RestTemplate restTemplate;
    private final String musicPlatformServiceUrl;

    @Autowired
    public TrackClient(RestTemplate restTemplate, MusicPlatformServiceConfig musicPlatformServiceConfig) {
        this.restTemplate = restTemplate;
        this.musicPlatformServiceUrl = musicPlatformServiceConfig.getMusicPlatformServiceUrl();
    }

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
