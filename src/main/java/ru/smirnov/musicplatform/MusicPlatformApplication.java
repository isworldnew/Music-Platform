package ru.smirnov.musicplatform;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class MusicPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicPlatformApplication.class, args);
	}

}
