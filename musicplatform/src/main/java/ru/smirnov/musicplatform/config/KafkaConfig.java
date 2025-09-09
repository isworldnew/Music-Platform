package ru.smirnov.musicplatform.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    /*
    может, если аккаунт админа перестаёт быть enabled - об этом тоже уведомлять?
    то есть: event-driven архитектура не только на случай регистрации нового админа, но и
    на случай изменения статуса его аккаунта...

    возможно понадобится ещё и реализация идемпотентности

    и глянь скриншоты в телеге
    */
}
