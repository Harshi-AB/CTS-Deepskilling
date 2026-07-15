import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * GetMapping.java
 *
 * Local, functionally-equivalent stand-in for Spring's
 * org.springframework.web.bind.annotation.GetMapping annotation, used
 * here because this project uses Core Java only. Documents the URL
 * that a controller method is mapped to for HTTP GET requests. The
 * value is also used at runtime by MainClass to register the real
 * endpoint on the embedded HTTP server (com.sun.net.httpserver).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GetMapping {
    String value();
}
