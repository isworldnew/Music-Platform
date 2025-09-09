package ru.smirnov.musicplatform.kafka.producer.abstraction;

import ru.smirnov.dtoregistry.message.AdminDataMessage;

public interface KafkaAdminProducer {

    void sendMessage(AdminDataMessage message);
}
