package ru.smirnov.musicplatform.kafka.consumer.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.smirnov.dtoregistry.message.TrackStatusMessage;
import ru.smirnov.musicplatform.dto.domain.track.TrackAccessLevelRequest;
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
        try {
            TrackStatusMessage message = this.objectMapper.readValue(jsonMessage, TrackStatusMessage.class);
            this.trackService.updateTrackAccessLevel(
                    message.getTrackId(),
                    new TrackAccessLevelRequest(message.getStatus().name()),
                    message.getTokenData()
            );
        }
        catch (JsonProcessingException e) {
            System.err.println("Deserialization error for message: " + jsonMessage);
            throw new RuntimeException(e);
        }
        catch (Exception e) {
            System.err.println("Global exception during message consuming");
            throw new RuntimeException(e);
        }
    }
}
