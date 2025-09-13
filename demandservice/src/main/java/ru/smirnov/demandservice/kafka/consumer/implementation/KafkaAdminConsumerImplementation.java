package ru.smirnov.demandservice.kafka.consumer.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.smirnov.demandservice.kafka.consumer.abstraction.KafkaAdminConsumer;
import ru.smirnov.demandservice.service.abstraction.domain.AdminDataService;
import ru.smirnov.demandservice.kafka.consumer.implementation.AdminDataMessage;

@Service
public class KafkaAdminConsumerImplementation implements KafkaAdminConsumer {

    private final AdminDataService adminDataService;
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaAdminConsumerImplementation(AdminDataService adminDataService, ObjectMapper objectMapper) {
        this.adminDataService = adminDataService;
        this.objectMapper = objectMapper;
    }

    @Override
    @KafkaListener(topics = "${spring.kafka.topic.admins.name}", groupId = "${spring.kafka.consumer.admins.group-id}")
    public void consume(String jsonMessage) {
        try {
            AdminDataMessage message = this.objectMapper.readValue(jsonMessage, AdminDataMessage.class);
            this.adminDataService.saveAdminData(message);
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
