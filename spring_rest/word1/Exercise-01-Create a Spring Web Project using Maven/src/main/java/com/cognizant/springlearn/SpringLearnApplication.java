package com.cognizant.springlearn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringLearnApplication
 *
 * Entry point of the Spring Boot web application created via Spring
 * Initializr (https://start.spring.io) with Group "com.cognizant",
 * Artifact "spring-learn", and the "Spring Web" + "Spring Boot DevTools"
 * dependencies selected.
 *
 * The @SpringBootApplication annotation is a convenience annotation that
 * combines:
 *   - @Configuration      : marks this class as a source of bean definitions
 *   - @EnableAutoConfiguration : tells Spring Boot to auto-configure beans
 *                                based on the jars present on the classpath
 *   - @ComponentScan      : tells Spring to scan this package (and sub
 *                           packages) for components, configurations, and
 *                           services
 */
@SpringBootApplication
public class SpringLearnApplication {

    /**
     * main() method - the entry point of the Java Virtual Machine (JVM)
     * as well as the Spring Boot application.
     *
     * SpringApplication.run() bootstraps the application, starts the
     * embedded Tomcat server (because spring-boot-starter-web is on the
     * classpath), creates the Spring ApplicationContext, and runs any
     * CommandLineRunner / ApplicationRunner beans.
     */
    public static void main(String[] args) {
        System.out.println("main() method of SpringLearnApplication invoked - starting application...");
        SpringApplication.run(SpringLearnApplication.class, args);
        System.out.println("SpringLearnApplication started successfully.");
    }
}
