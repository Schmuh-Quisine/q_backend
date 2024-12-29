package org.schmuh.quisine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.lang.module.Configuration;

/**
 * The entry point of the Spring Boot application.
 *
 * <p>This class contains the main method that starts the Spring Boot application.
 * It is annotated with {@link SpringBootApplication}, which includes several other annotations such as
 * {@link Configuration}, {@link EnableAutoConfiguration}, and {@link ComponentScan}, allowing Spring to
 * automatically configure and discover components within the application.
 */

@SpringBootApplication(scanBasePackages = "org.schmuh.quisine")
public class QuisineApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuisineApplication.class, args);
	}

}