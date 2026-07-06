import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * LoggingInvocationHandler.java
 *
 * This class is the heart of our AOP simulation. It implements
 * java.lang.reflect.InvocationHandler, which lets us intercept every
 * method call made on a dynamic proxy - conceptually identical to how
 * Spring AOP generates a JDK dynamic proxy and routes calls through
 * MethodInterceptor / Advice chains.
 *
 * It logs:
 *   - Before advice  -> before the target method runs
 *   - After advice   -> after the target method returns successfully
 *   - Around advice  -> timing information wrapping the whole call
 *   - Exception advice -> if the target method throws
 */
public class LoggingInvocationHandler implements InvocationHandler {

    private final Object target;

    public LoggingInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // ----- Before advice -----
        System.out.println("[LOG] Before  : " + method.getName() + "(" + argsToString(args) + ")");

        long startTime = System.nanoTime();
        try {
            // Delegate to the real target object
            Object result = method.invoke(target, args);

            long elapsedMicros = (System.nanoTime() - startTime) / 1000;

            // ----- After (returning) advice -----
            System.out.println("[LOG] After   : " + method.getName() + " completed in " + elapsedMicros + " micros");
            return result;

        } catch (Exception ex) {
            // ----- After-throwing advice -----
            System.out.println("[LOG] Error   : " + method.getName() + " threw " + ex.getCause());
            throw ex;
        }
    }

    private String argsToString(Object[] args) {
        if (args == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]);
            if (i < args.length - 1) sb.append(", ");
        }
        return sb.toString();
    }
}
