/**
 * GreetingController.java
 *
 * A @Component bean that also implements CommandLineRunner, so the
 * SpringApplication runner will automatically execute its run()
 * method once the application context is ready - the same behaviour
 * Spring Boot provides out of the box.
 */
@Component
public class GreetingController implements CommandLineRunner {

    @Autowired
    private GreetingRepository greetingRepository;

    @Override
    public void run() {
        System.out.println(greetingRepository.fetchGreeting());
    }
}
