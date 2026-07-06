/**
 * ConfiguringBeansWithAnnotations.java
 *
 * Demonstrates annotation-driven bean configuration using our own
 * @Component / @Autowired annotations and AnnotationConfigContainer,
 * since real Spring annotations/container cannot be used here.
 *
 * Flow:
 *   1. "Scan" a fixed set of candidate classes for @Component.
 *   2. Let the container auto-wire @Autowired fields by type.
 *   3. Retrieve the fully wired UserService bean and use it.
 */
public class ConfiguringBeansWithAnnotations {

    public static void main(String[] args) {
        AnnotationConfigContainer context = new AnnotationConfigContainer();

        // Simulates classpath component scanning of these two classes
        context.scanAndRegister(UserRepository.class, UserService.class);

        UserService userService = context.getBean(UserService.class);
        System.out.println("\nOutput: " + userService.getUserDetails(101));
    }
}
