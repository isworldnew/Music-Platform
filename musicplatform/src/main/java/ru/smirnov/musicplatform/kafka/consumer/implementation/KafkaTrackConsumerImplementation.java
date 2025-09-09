package ru.smirnov.musicplatform.kafka.consumer.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.kafka.consumer.abstraction.KafkaTrackConsumer;
import ru.smirnov.musicplatform.service.abstraction.domain.TrackService;

@Service
public class KafkaTrackConsumerImplementation implements KafkaTrackConsumer {

    private final TrackService trackService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public KafkaTrackConsumerImplementation(TrackService trackService) {
        this.trackService = trackService;
    }

    @Override
    @KafkaListener(topics = "${spring.kafka.topic.tracks.name}", groupId = "${spring.kafka.consumer.tracks.group-id}")
    public void consume(String jsonMessage) {

    }
}
