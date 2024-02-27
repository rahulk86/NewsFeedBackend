package com.NewFeed.backend;

import jakarta.persistence.EntityListeners;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.auth.modal","com.NewFeed.backend.modal"})
@EnableJpaRepositories(basePackages = {"com.auth.repository","com.NewFeed.backend.repository"})
@ComponentScan(basePackages = {"com.auth","com.NewFeed.backend"})
@EntityListeners(AuditingEntityListener.class)
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
