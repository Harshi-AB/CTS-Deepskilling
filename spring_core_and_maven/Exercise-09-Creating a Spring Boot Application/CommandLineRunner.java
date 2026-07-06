/**
 * CommandLineRunner.java
 *
 * Mirrors Spring Boot's org.springframework.boot.CommandLineRunner
 * interface. Any @Component bean implementing this interface has its
 * run() method invoked automatically right after the application
 * context has started up - exactly like real Spring Boot does.
 */
public interface CommandLineRunner {
    void run() throws Exception;
}
