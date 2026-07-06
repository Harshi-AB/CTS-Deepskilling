import java.lang.reflect.Method;

/**
 * AfterAdvice.java
 *
 * Functional-style interface representing "after returning" advice,
 * mirroring Spring AOP's org.springframework.aop.AfterReturningAdvice.
 * Implementations run after the target method completes successfully.
 */
public interface AfterAdvice {
    void after(Method method, Object result, Object[] args, Object target);
}
