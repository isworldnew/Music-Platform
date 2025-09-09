package ru.smirnov.demandservice.kafka.producer.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.smirnov.demandservice.config.KafkaConfig;
import ru.smirnov.demandservice.kafka.producer.abstraction.KafkaDistributorProducer;
import ru.smirnov.dtoregistry.message.DistributorRegistrationMessage;

import java.util.UUID;

@Service
public class KafkaDistributorProducerImplementation implements KafkaDistributorProducer {

    private final KafkaTemplate<String, DistributorRegistrationMessage> kafkaTemplate;
    private String distributorPostTopic;

    @Autowired
    public KafkaDistributorProducerImplementation(KafkaTemplate<String, DistributorRegistrationMessage> kafkaTemplate, KafkaConfig kafkaConfig) {
        this.kafkaTemplate = kafkaTemplate;
        this.distributorPostTopic = kafkaConfig.getDistributorPostTopic();
    }

    @Override
    public void sendMessage(DistributorRegistrationMessage message) {
        String partitionKey = UUID.randomUUID().toString();

        this.kafkaTemplate.send(
                this.distributorPostTopic,
                partitionKey,
                message
        );
    }

}
