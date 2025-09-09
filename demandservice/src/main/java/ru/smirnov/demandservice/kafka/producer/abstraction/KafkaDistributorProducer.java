package ru.smirnov.demandservice.kafka.producer.abstraction;

import ru.smirnov.dtoregistry.message.DistributorRegistrationMessage;

public interface KafkaDistributorProducer {

    void sendMessage(DistributorRegistrationMessage message);
}
