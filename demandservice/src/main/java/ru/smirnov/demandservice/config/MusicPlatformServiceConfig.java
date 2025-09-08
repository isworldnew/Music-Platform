package ru.smirnov.demandservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration @Getter
public class MusicPlatformServiceConfig {

    @Value("${musicplatform.service.url}")
    private String musicPlatformServiceUrl;

}
