package ru.smirnov.musicplatform.kafka.consumer.abstraction;

public interface KafkaTrackConsumer {

    void consume(String jsonMessage);
}
