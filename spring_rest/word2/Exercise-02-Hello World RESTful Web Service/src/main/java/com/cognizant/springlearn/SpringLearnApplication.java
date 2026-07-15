package com.cognizant.springlearn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringLearnApplication
 *
 * Entry point for the Spring Boot application used throughout the
 * Digital Nurture 5.0 - Spring REST hands-on exercises.
 *
 * Run with: mvn spring-boot:run
 *   or:     java -jar target/spring-learn.jar
 *
 * Once running, browse to http://localhost:8083/hello
 */
@SpringBootApplication
public class SpringLearnApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringLearnApplication.class, args);
    }
}
