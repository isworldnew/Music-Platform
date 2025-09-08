package ru.smirnov.demandservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import ru.smirnov.demandservice.config.MusicPlatformServiceConfig;
import ru.smirnov.dtoregistry.exception.ExternalServiceException;
import ru.smirnov.dtoregistry.exception.NotFoundException;

import java.util.List;

@Service
public class AdminClient {

    private final RestTemplate restTemplate;
    private final String musicPlatformServiceUrl;

    @Autowired
    public AdminClient(RestTemplate restTemplate, MusicPlatformServiceConfig musicPlatformServiceConfig) {
        this.restTemplate = restTemplate;
        this.musicPlatformServiceUrl = musicPlatformServiceConfig.getMusicPlatformServiceUrl();
    }

    public List<Long> getAllEnabledAdmins() {
        String url = this.musicPlatformServiceUrl + "/admins/enabled";

        try {
            List<Long> admins = this.restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Long>>() {}
            ).getBody();

            if (admins == null || admins.isEmpty())
                throw new NotFoundException("No enabled admins found");

            return admins;
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ExternalServiceException("Failed to fetch enabled admins");
        }
    }

}
