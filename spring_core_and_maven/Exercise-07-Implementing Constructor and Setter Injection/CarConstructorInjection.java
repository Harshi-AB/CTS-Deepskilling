/**
 * CarConstructorInjection.java
 *
 * Demonstrates Constructor Injection: the Engine dependency is
 * required and supplied at the moment the object is created, and
 * cannot be null or omitted. This is the DI style Spring recommends
 * for mandatory dependencies.
 */
public class CarConstructorInjection {

    private final Engine engine;

    public CarConstructorInjection(Engine engine) {
        if (engine == null) {
            throw new IllegalArgumentException("Engine dependency must not be null");
        }
        this.engine = engine;
    }

    public void drive() {
        System.out.println("[Constructor Injection] " + engine.start());
    }
}
