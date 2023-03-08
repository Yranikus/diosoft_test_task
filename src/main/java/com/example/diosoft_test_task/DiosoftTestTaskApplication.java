package com.example.diosoft_test_task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class DiosoftTestTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiosoftTestTaskApplication.class, args);
	}

}
