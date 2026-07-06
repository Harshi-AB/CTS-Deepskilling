import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SpringBootApplication.java
 *
 * Custom annotation mirroring org.springframework.boot.autoconfigure.
 * SpringBootApplication. It marks the main class of the application,
 * telling our own mini "SpringApplication" runner where to start.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SpringBootApplication {
}
