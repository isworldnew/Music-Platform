package ru.smirnov.demandservice.kafka.producer.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.smirnov.demandservice.config.KafkaConfig;
import ru.smirnov.demandservice.kafka.producer.abstraction.KafkaTrackProducer;
import ru.smirnov.dtoregistry.message.TrackStatusMessage;

import java.util.UUID;

@Service
public class KafkaTrackProducerImplementation implements KafkaTrackProducer {

    private final KafkaTemplate<String, TrackStatusMessage> kafkaTemplate;
    private final String trackPatchTopic;

    @Autowired
    public KafkaTrackProducerImplementation(KafkaTemplate<String, TrackStatusMessage> kafkaTemplate, KafkaConfig kafkaConfig) {
        this.kafkaTemplate = kafkaTemplate;
        this.trackPatchTopic = kafkaConfig.getTrackPatchTopic();
    }

    @Override
    public void sendMessage(TrackStatusMessage message) {
        String partitionKey = UUID.randomUUID().toString();

        this.kafkaTemplate.send(
                this.trackPatchTopic,
                partitionKey,
                message
        );
    }

}
