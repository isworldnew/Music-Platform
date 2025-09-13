package ru.smirnov.musicplatform.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.messages.JsonType;
import lombok.Getter;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration @Getter
public class KafkaConfig {

    @Value("${spring.kafka.topic.admins.name}")
    private String adminPostTopic;

    @Bean
    public NewTopic createAdminPostTopic() {
        return TopicBuilder.name(this.adminPostTopic)
                .partitions(2)
                .replicas(3)
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
