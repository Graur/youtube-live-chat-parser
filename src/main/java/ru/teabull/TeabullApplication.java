package ru.teabull;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("ru.teabull.teabull.dao")
@EntityScan("ru.teabull.teabull.mvc.model")
@SpringBootApplication
public class TeabullApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeabullApplication.class, args);
	}
}
