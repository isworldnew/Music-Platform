package ru.smirnov.musicplatform.kafka.consumer.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.smirnov.musicplatform.kafka.consumer.abstraction.KafkaDistributorConsumer;
import ru.smirnov.musicplatform.service.abstraction.audience.DistributorService;

@Service
public class KafkaDistributorConsumerImplementation implements KafkaDistributorConsumer {

    private final DistributorService distributorService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public KafkaDistributorConsumerImplementation(DistributorService distributorService) {
        this.distributorService = distributorService;
    }

    @Override
    @KafkaListener(topics = "${spring.kafka.topic.distributors.name}", groupId = "${distributors-consumer-group}")
    public void consume(String jsonMessage) {
        // создать аккаунт дистрибьютору и записать бизнес-данные дистрибьютора
    }
}
