package ru.smirnov.demandservice.kafka.producer.abstraction;

import ru.smirnov.dtoregistry.message.TrackStatusMessage;

public interface KafkaTrackProducer {

    void sendMessage(TrackStatusMessage message);
}
