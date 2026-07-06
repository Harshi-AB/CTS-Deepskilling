/**
 * CarSetterInjection.java
 *
 * Demonstrates Setter Injection: the Engine dependency is optional at
 * construction time and is instead supplied afterwards through a
 * setter method. This DI style is useful for optional or
 * re-configurable dependencies, matching Spring's setter-based
 * injection support.
 */
public class CarSetterInjection {

    private Engine engine;

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void drive() {
        if (engine == null) {
            System.out.println("[Setter Injection] No engine has been set yet!");
            return;
        }
        System.out.println("[Setter Injection] " + engine.start());
    }
}
