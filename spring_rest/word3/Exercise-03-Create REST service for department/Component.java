import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Component.java
 *
 * This project does not use the real Spring Framework (Core Java only,
 * no Maven/Gradle, no external jars). To still honour the exercise
 * requirement of annotating classes the way Spring would
 * (@Component, @Service, @Transactional, @GetMapping), this file
 * defines a local, functionally-equivalent marker annotation named
 * after Spring's org.springframework.stereotype.Component.
 *
 * It documents that the annotated class is a Spring-managed bean/
 * component, even though no IoC container instantiates it here -
 * instantiation is instead done explicitly in the Main class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {
}
