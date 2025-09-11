package ru.smirnov.demandservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration @Getter
public class KafkaConfig {

    @Value("${spring.kafka.topic.tracks.name}")
    private String trackPatchTopic;

    @Value("${spring.kafka.topic.distributors.name}")
    private String distributorPostTopic;

    @Bean
    public NewTopic createTrackPatchTopic() {
        return TopicBuilder.name(this.trackPatchTopic)
                .partitions(2)
                .replicas(3)
                .build();
    }

    @Bean
    public NewTopic createDistributorPostTopic() {
        return TopicBuilder.name(this.distributorPostTopic)
                .partitions(2)
                .replicas(3)
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
