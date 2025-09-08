package ru.smirnov.demandservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.smirnov.demandservice.config.MusicPlatformServiceConfig;

@Service
public class TrackClient {

    private final RestTemplate restTemplate;
    private final String musicPlatformServiceUrl;

    @Autowired
    public TrackClient(RestTemplate restTemplate, MusicPlatformServiceConfig musicPlatformServiceConfig) {
        this.restTemplate = restTemplate;
        this.musicPlatformServiceUrl = musicPlatformServiceConfig.getMusicPlatformServiceUrl();
    }

    public ResponseEntity<?> trackExistsById(Long trackId) {
        String url = this.musicPlatformServiceUrl + "";

        return this.restTemplate.getForEntity()
    }

}
