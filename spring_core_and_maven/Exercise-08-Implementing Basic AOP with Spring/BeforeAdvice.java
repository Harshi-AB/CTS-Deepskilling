import java.lang.reflect.Method;

/**
 * BeforeAdvice.java
 *
 * Functional-style interface representing "before" advice, mirroring
 * Spring AOP's org.springframework.aop.MethodBeforeAdvice.
 * Implementations run just before the target method executes.
 */
public interface BeforeAdvice {
    void before(Method method, Object[] args, Object target);
}
