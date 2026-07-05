import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation used to mark a method as a JUnit-style test case.
 *
 * Since this project uses plain "javac *.java" with no build tool (Maven/Gradle),
 * the real org.junit.Test annotation is not on the classpath. This annotation
 * reproduces the same purpose: marking a no-argument method that should be
 * picked up and executed by the TestRunner using reflection.
 *
 * RetentionPolicy.RUNTIME ensures the annotation is available at runtime,
 * which is required so that TestRunner can discover it via reflection.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
}
