/**
 * CreatingASpringBootApplication.java
 *
 * Entry point simulating a Spring Boot application's main class,
 * since the real Spring Boot framework cannot be used in this
 * exercise. Annotated with our own @SpringBootApplication and started
 * via our own SpringApplication.run(), exactly mirroring the standard
 *   @SpringBootApplication
 *   public class App {
 *       public static void main(String[] args) {
 *           SpringApplication.run(App.class, args);
 *       }
 *   }
 * pattern used in real Spring Boot projects.
 */
@SpringBootApplication
public class CreatingASpringBootApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(
                CreatingASpringBootApplication.class,
                GreetingRepository.class,
                GreetingController.class
        );
    }
}
