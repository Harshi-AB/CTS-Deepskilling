import java.lang.reflect.Method;

/**
 * LoggingBeforeAdvice.java
 *
 * Concrete "before" advice implementation that logs the method call
 * about to happen.
 */
public class LoggingBeforeAdvice implements BeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) {
        System.out.println("[BEFORE ADVICE] About to call " + method.getName() +
                " on " + target.getClass().getSimpleName());
    }
}
