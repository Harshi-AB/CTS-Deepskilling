import java.lang.reflect.Method;

/**
 * LoggingAfterAdvice.java
 *
 * Concrete "after returning" advice implementation that logs the
 * result produced by the target method.
 */
public class LoggingAfterAdvice implements AfterAdvice {
    @Override
    public void after(Method method, Object result, Object[] args, Object target) {
        System.out.println("[AFTER ADVICE] " + method.getName() + " returned: " + result);
    }
}
