package ru.smirnov.musicplatform.kafka.consumer.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.smirnov.dtoregistry.message.DistributorRegistrationMessage;
import ru.smirnov.musicplatform.kafka.consumer.abstraction.KafkaDistributorConsumer;
import ru.smirnov.musicplatform.service.abstraction.audience.DistributorService;

@Service
public class KafkaDistributorConsumerImplementation implements KafkaDistributorConsumer {

    private final DistributorService distributorService;
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaDistributorConsumerImplementation(DistributorService distributorService, ObjectMapper objectMapper) {
        this.distributorService = distributorService;
        this.objectMapper = objectMapper;
    }

    @Override
    @KafkaListener(topics = "${spring.kafka.topic.distributors.name}", groupId = "${spring.kafka.consumer.distributors.group-id}")
    public void consume(String jsonMessage) {
        try {
            DistributorRegistrationMessage message = this.objectMapper.readValue(jsonMessage, DistributorRegistrationMessage.class);
            this.distributorService.distributorRegistration(message);
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
