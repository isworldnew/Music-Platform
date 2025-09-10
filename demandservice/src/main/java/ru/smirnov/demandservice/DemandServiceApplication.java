package ru.smirnov.demandservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class DemandServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemandServiceApplication.class, args);
	}

}
