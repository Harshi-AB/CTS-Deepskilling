package com.cognizant.springlearn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * SpringLearnApplication
 *
 * Entry point for the Spring Boot application used throughout the
 * Digital Nurture 5.0 - Spring REST hands-on exercises.
 *
 * @ImportResource loads the legacy Spring XML bean configuration
 * (country.xml) so the "indiaCountry" / "countryList" beans defined there become
 * part of the Spring Boot application context and can be injected
 * into CountryController.
 *
 * Run with: mvn spring-boot:run
 *   or:     java -jar target/spring-learn.jar
 *
 * Once running, browse to http://localhost:8083/hello or /country
 */
@SpringBootApplication
@ImportResource("classpath:country.xml")
public class SpringLearnApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringLearnApplication.class, args);
    }
}
