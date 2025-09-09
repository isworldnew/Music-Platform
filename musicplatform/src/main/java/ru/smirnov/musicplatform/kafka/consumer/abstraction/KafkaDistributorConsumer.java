package ru.smirnov.musicplatform.kafka.consumer.abstraction;

public interface KafkaDistributorConsumer {
    void consume(String jsonMessage);
}
