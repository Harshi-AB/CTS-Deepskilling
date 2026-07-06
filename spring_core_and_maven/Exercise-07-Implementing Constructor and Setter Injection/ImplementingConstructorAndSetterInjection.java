/**
 * ImplementingConstructorAndSetterInjection.java
 *
 * Entry point comparing Constructor Injection and Setter Injection
 * side by side, without relying on the Spring framework.
 */
public class ImplementingConstructorAndSetterInjection {

    public static void main(String[] args) {
        // ----- Constructor Injection -----
        Engine petrolEngine = new PetrolEngine();
        CarConstructorInjection carA = new CarConstructorInjection(petrolEngine);
        carA.drive();

        // ----- Setter Injection -----
        CarSetterInjection carB = new CarSetterInjection();
        carB.drive(); // dependency not set yet

        Engine electricEngine = new ElectricEngine();
        carB.setEngine(electricEngine);
        carB.drive(); // dependency now set

        // Setter injection also allows swapping the dependency later
        carB.setEngine(new PetrolEngine());
        carB.drive();
    }
}
