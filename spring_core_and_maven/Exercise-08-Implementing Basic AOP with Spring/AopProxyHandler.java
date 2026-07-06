import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * AopProxyHandler.java
 *
 * Custom InvocationHandler that applies a chain of BeforeAdvice and
 * AfterAdvice around a target object's method calls, plus its own
 * "around" style timing logic. This plays the same structural role as
 * Spring AOP's internal ReflectiveMethodInvocation, which walks an
 * advice chain around the real method invocation.
 */
public class AopProxyHandler implements InvocationHandler {

    private final Object target;
    private final List<BeforeAdvice> beforeAdvices;
    private final List<AfterAdvice> afterAdvices;

    public AopProxyHandler(Object target, List<BeforeAdvice> beforeAdvices, List<AfterAdvice> afterAdvices) {
        this.target = target;
        this.beforeAdvices = beforeAdvices;
        this.afterAdvices = afterAdvices;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Apply all "before" advices
        for (BeforeAdvice advice : beforeAdvices) {
            advice.before(method, args, target);
        }

        // ----- Around advice: timing wraps the actual invocation -----
        long start = System.nanoTime();
        Object result;
        try {
            result = method.invoke(target, args);
        } catch (Exception ex) {
            System.out.println("[AROUND ADVICE] Exception during " + method.getName() + ": " + ex.getCause());
            throw ex;
        }
        long durationMicros = (System.nanoTime() - start) / 1000;
        System.out.println("[AROUND ADVICE] " + method.getName() + " took " + durationMicros + " micros");

        // Apply all "after returning" advices
        for (AfterAdvice advice : afterAdvices) {
            advice.after(method, result, args, target);
        }

        return result;
    }
}
