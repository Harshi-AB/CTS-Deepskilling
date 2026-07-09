/**
 * Custom re-implementation of org.springframework.boot.SpringApplication.
 *
 * Real Spring Boot's SpringApplication.run(MainClass.class, args) scans the
 * package for @Component/@Service/@Repository classes, wires them, starts an
 * embedded server (if web), and returns the ApplicationContext. We only need
 * the "build and return the context" part for these console demos.
 */
public class SpringApplication {

    public static ApplicationContext run(Class<?> mainClass, String[] args) {
        Logger logger = LoggerFactory.getLogger(SpringApplication.class);
        logger.info("Starting {} using Java {}", mainClass.getSimpleName(), System.getProperty("java.version"));
        ApplicationContext context = new ApplicationContext();
        logger.info("Started {} - ApplicationContext initialised", mainClass.getSimpleName());
        return context;
    }
}
