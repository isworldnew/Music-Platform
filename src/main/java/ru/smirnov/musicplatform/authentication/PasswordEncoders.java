package ru.smirnov.musicplatform.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordEncoders {

    // brute-force способ избавиться от циклической зависимости между WebSecurityConfig и UserDetailsService

    // Для того, чтобы создалась конфигурация, ей нужен сервис.
    // А чтобы создался сервис, ему нужен бин энкодера, который создаётся в конфигурации.
    // А чтобы конфигурация его создала, ей для начала самой нужно создаться (чего она не может сделать без сервиса)

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
