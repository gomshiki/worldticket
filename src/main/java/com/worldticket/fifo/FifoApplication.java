package com.worldticket.fifo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FifoApplication {
	public static void main(String[] args) {
		SpringApplication.run(FifoApplication.class, args);
	}
}
