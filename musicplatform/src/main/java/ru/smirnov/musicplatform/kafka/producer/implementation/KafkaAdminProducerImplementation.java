package ru.smirnov.musicplatform.kafka.producer.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.smirnov.dtoregistry.message.AdminDataMessage;
import ru.smirnov.musicplatform.config.KafkaConfig;
import ru.smirnov.musicplatform.kafka.producer.abstraction.KafkaAdminProducer;

import java.util.UUID;

@Service
public class KafkaAdminProducerImplementation implements KafkaAdminProducer {

    private final KafkaTemplate<String, AdminDataMessage> kafkaTemplate;
    private final String adminPostTopic;

    @Autowired
    public KafkaAdminProducerImplementation(KafkaTemplate<String, AdminDataMessage> kafkaTemplate, KafkaConfig kafkaConfig) {
        this.kafkaTemplate = kafkaTemplate;
        this.adminPostTopic = kafkaConfig.getAdminPostTopic();
    }

    @Override
    public void sendMessage(AdminDataMessage message) {
        String partitionKey = UUID.randomUUID().toString();

        this.kafkaTemplate.send(
                this.adminPostTopic,
                partitionKey,
                message
        );
    }

}
