package ru.smirnov.demandservice.kafka.consumer.abstraction;

public interface KafkaAdminConsumer {
    void consume(String jsonMessage);
}
