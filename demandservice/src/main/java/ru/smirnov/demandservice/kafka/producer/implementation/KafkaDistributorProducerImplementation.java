package ru.smirnov.demandservice.kafka.producer.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.smirnov.demandservice.config.KafkaConfig;
import ru.smirnov.demandservice.kafka.producer.abstraction.KafkaDistributorProducer;
import ru.smirnov.dtoregistry.dto.kafka.DistributorRegistrationRequest;

@Service
public class KafkaDistributorProducerImplementation implements KafkaDistributorProducer {

    private final KafkaTemplate<String, DistributorRegistrationRequest> kafkaTemplate;
    private String distributorPostTopicName;

    @Autowired
    public KafkaDistributorProducerImplementation(KafkaTemplate<String, DistributorRegistrationRequest> kafkaTemplate, KafkaConfig kafkaConfig) {
        this.kafkaTemplate = kafkaTemplate;
        this.distributorPostTopicName = kafkaConfig.getDistributorPostTopic();
    }



}
