package ru.smirnov.demandservice.kafka.producer.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.smirnov.demandservice.config.KafkaConfig;
import ru.smirnov.demandservice.kafka.producer.abstraction.KafkaTrackProducer;

@Service
public class KafkaTrackProducerImplementation implements KafkaTrackProducer {

    private final KafkaTemplate<String, ?> kafkaTemplate;
    private final String trackPatchTopicname;

    @Autowired
    public KafkaTrackProducerImplementation(KafkaTemplate<String, ?> kafkaTemplate, KafkaConfig kafkaConfig) {
        this.kafkaTemplate = kafkaTemplate;
        this.trackPatchTopicname = kafkaConfig.getTrackPatchTopic();
    }


}
